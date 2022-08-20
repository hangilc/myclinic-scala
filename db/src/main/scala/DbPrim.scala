package dev.myclinic.scala.db

import dev.myclinic.scala.model.*
import cats.*
import cats.syntax.all.*
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler.jdkLogHandler
import dev.myclinic.scala.db.DoobieMapping._
import dev.myclinic.scala.db.DbTextPrim.countTextForVisit
import dev.myclinic.scala.db.DbDrugPrim.countDrugForVisit
import dev.myclinic.scala.db.DbShinryouPrim.countShinryouForVisit
import dev.myclinic.scala.db.DbConductPrim.countConductForVisit
import dev.myclinic.scala.db.DbChargePrim.countChargeForVisit
import dev.myclinic.scala.db.DbPaymentPrim.countPaymentForVisit
import cats.data.EitherT

object DbPrim:
  type ShinryouId = Int
  type ConductId = Int

  def deleteVisit(visitId: Int): ConnectionIO[List[AppEvent]] =
    def check(
        chk: Int => ConnectionIO[Int],
        err: String
    ): EitherT[ConnectionIO, String, Unit] =
      EitherT(chk(visitId).map(c => if c > 0 then Left(err) else Right(())))
    def proc(): ConnectionIO[List[AppEvent]] =
        for
          wqueueEventOpt <- DbWqueuePrim.tryDeleteWqueue(visitId)
          visit <- DbVisitPrim.getVisit(visitId).unique
          _ <- DbVisitPrim.deleteVisit(visitId)
          visitEvent <- DbEventPrim.logVisitDeleted(visit)
        yield wqueueEventOpt.toList :+ visitEvent
    val op = List(
      check(countTextForVisit, "テキストがあるため、削除できません。"),
      check(countDrugForVisit, "処方があるため、削除できません。"),
      check(countShinryouForVisit, "診療行為があるため、削除できません。"),
      check(countConductForVisit, "処置があるため、削除できません。"),
      check(countChargeForVisit, "請求があるため、削除できません。"),
      check(countPaymentForVisit, "支払い記録があるため、削除できません。")
    ).sequence_.value
    for
      either <- op
      _ = either match {
        case Right(_)  => ()
        case Left(msg) => throw new RuntimeException(msg)
      }
      events <- proc()
    yield events

  def batchEnterShinryouConduct(
      req: CreateShinryouConductRequest
  ): ConnectionIO[(List[AppEvent], List[ShinryouId], List[ConductId])] =
    for
      shinryouResult <- req.shinryouList
        .map(DbShinryouPrim.enterShinryou(_).map { case (event, entered) =>
          (event, entered.shinryouId)
        })
        .sequence
      (shinryouEvents, shinryouIds) = shinryouResult.unzip
      conductResult <- req.conducts.map(DbConductPrim.createConduct(_)).sequence
      (conductEvents, conductIds) = conductResult.unzip
    yield (
      shinryouEvents ++ conductEvents.flatten,
      shinryouIds,
      conductIds
    )

  def listDiseaseAdjEx(
      diseaseId: Int
  ): ConnectionIO[List[(DiseaseAdj, ShuushokugoMaster)]] =
    sql"""
        select a.*, m.* from disease_adj as a inner join shuushokugo_master m
        on a.shuushokugocode = m.shuushokugocode
        where a.disease_id = ${diseaseId}
        order by a.disease_adj_id
    """.query[(DiseaseAdj, ShuushokugoMaster)].to[List]

  def listCurrentDiseaseEx(
      patientId: Int
  ): ConnectionIO[
    List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]
  ] =
    val diseaseOp = sql"""
        select d.*, m.* from disease as d inner join shoubyoumei_master_arch as m 
        on d.shoubyoumeicode = m.shoubyoumeicode and m.valid_from <= d.start_date
        and (m.valid_upto = '0000-00-00' or d.start_date <= m.valid_upto)
        where d.patient_id = ${patientId} and d.end_date = '0000-00-00'
        order by d.start_date
    """.query[(Disease, ByoumeiMaster)]
    for
      dlist <- diseaseOp.to[List]
      result <- (dlist.map { case (d, bm) =>
        listDiseaseAdjEx(d.diseaseId)
          .map(adjList => (d, bm, adjList))
      }).sequence
    yield result

  def listDiseaseEx(
      patientId: Int
  ): ConnectionIO[
    List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]
  ] =
    val diseaseOp = sql"""
          select d.*, m.* from disease as d inner join shoubyoumei_master_arch as m 
          on d.shoubyoumeicode = m.shoubyoumeicode and m.valid_from <= d.start_date
          and (m.valid_upto = '0000-00-00' or d.start_date <= m.valid_upto)
          where d.patient_id = ${patientId}
          order by d.start_date
      """.query[(Disease, ByoumeiMaster)]
    for
      dlist <- diseaseOp.to[List]
      result <- (dlist.map { case (d, bm) =>
        listDiseaseAdjEx(d.diseaseId)
          .map(adjList => (d, bm, adjList))
      }).sequence
    yield result

  def getDiseaseEx(diseaseId: Int): ConnectionIO[
    (Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])
  ] =
    val diseaseOp = sql"""
          select d.*, m.* from disease as d inner join shoubyoumei_master_arch as m 
          on d.shoubyoumeicode = m.shoubyoumeicode and m.valid_from <= d.start_date
          and (m.valid_upto = '0000-00-00' or d.start_date <= m.valid_upto)
          where d.disease_id = ${diseaseId}
      """.query[(Disease, ByoumeiMaster)]
    for
      dex <- diseaseOp.unique
      (disease, bMaster) = dex
      adjEx <- listDiseaseAdjEx(disease.diseaseId)
    yield (disease, bMaster, adjEx)

  def listMishuuForPatient(
      patientId: Int,
      nVisits: Int
  ): ConnectionIO[List[(Visit, Charge)]] =
    for
      visits <- DbVisitPrim.listByPatientReverse(patientId, 0, nVisits)
      charges <- visits
        .map(visit => DbChargePrim.getCharge(visit.visitId).option)
        .sequence
      payments <- visits
        .map(visit => DbPaymentPrim.getLastPayment(visit.visitId).option)
        .sequence
    yield (visits.zip(charges).zip(payments) map {
      case ((visit, Some(charge)), Some(payment)) if payment.amount == 0 =>
        Some((visit, charge))
      case _ => None
    }) collect { case Some((visit, charge)) =>
      (visit, charge)
    }
