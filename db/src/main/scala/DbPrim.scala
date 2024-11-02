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
import java.time.LocalDateTime
import java.time.LocalDate

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
        onshiEventOpt <- DbOnshiPrim.clearOnshi(visitId)
        chargeEventOpt <- DbChargePrim.tryDeleteCharge(visitId)
        visit <- DbVisitPrim.getVisit(visitId).unique
        _ <- DbVisitPrim.deleteVisit(visitId)
        visitEvent <- DbEventPrim.logVisitDeleted(visit)
      yield wqueueEventOpt.toList ++ onshiEventOpt.toList ++ chargeEventOpt.toList :+ visitEvent
    val op = List(
      check(countTextForVisit, "テキストがあるため、削除できません。"),
      check(countDrugForVisit, "処方があるため、削除できません。"),
      check(countShinryouForVisit, "診療行為があるため、削除できません。"),
      check(countConductForVisit, "処置があるため、削除できません。"),
      // check(countChargeForVisit, "請求があるため、削除できません。"),
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

  def startVisitWithHoken(
      patientId: Int,
      at: LocalDateTime,
      shahokokuhoId: Int,
      koukikoureiId: Int,
      kouhiIds: List[Int]
  ): ConnectionIO[(Visit, List[AppEvent])] =
    val date = at.toLocalDate
    for
      shahokokuho <- DbShahokokuhoPrim.getShahokokuhoOpt(shahokokuhoId)
      _ = shahokokuho.foreach(h =>
        if !h.isValidAt(date) then
          throw new RuntimeException("Not valid at: " + date)
      )
      koukikourei <- DbKoukikoureiPrim.getKoukikoureiOpt(koukikoureiId)
      _ = koukikourei.foreach(h =>
        if !h.isValidAt(date) then
          throw new RuntimeException("Not valid at: " + date)
      )
      kouhiList <- kouhiIds
        .map(kouhiId => DbKouhiPrim.getKouhi(kouhiId).unique)
        .sequence
      _ = kouhiList.foreach(h =>
        if !h.isValidAt(date) then
          throw new RuntimeException("Not valid at: " + date)
      )
      roujinId = 0
      kouhi1Id = kouhiList.map(_.kouhiId).get(0).getOrElse(0)
      kouhi2Id = kouhiList.map(_.kouhiId).get(1).getOrElse(0)
      kouhi3Id = kouhiList.map(_.kouhiId).get(2).getOrElse(0)
      enterVisitResult <-
        val visit = Visit(
          0,
          patientId,
          at,
          shahokokuhoId,
          roujinId,
          kouhi1Id,
          kouhi2Id,
          kouhi3Id,
          koukikoureiId,
          None
        )
        DbVisitPrim.enterVisit(visit)
      (visitCreatedEvent, enteredVisit) = enterVisitResult
      wqCreatedEvent <-
        val wqueue = Wqueue(enteredVisit.visitId, WaitState.WaitExam)
        DbWqueuePrim.enterWqueue(wqueue)
    yield (enteredVisit, List(visitCreatedEvent, wqCreatedEvent))

  def newShahokokuho(
      hoken: Shahokokuho
  ): ConnectionIO[(Shahokokuho, List[AppEvent])] =
    val patientId = hoken.patientId
    val startDate: LocalDate = hoken.validFrom
    val endDate: LocalDate = startDate.minusDays(1);
    for
      shahokokuhoList <- DbShahokokuhoPrim.listAvailableShahokokuho(
        patientId,
        startDate
      )
      shahokokuhoEvents <- shahokokuhoList
        .map(shahokokuho => {
          val update = shahokokuho.copy(validUpto = ValidUpto(Some(endDate)))
          DbShahokokuhoPrim.updateShahokokuho(update)
        })
        .sequence
      koukikoureiList <- DbKoukikoureiPrim.listAvailableKoukikourei(
        patientId,
        startDate
      )
      koukikoureiEvents <- koukikoureiList
        .map(koukikourei => {
          val update = koukikourei.copy(validUpto = ValidUpto(Some(endDate)))
          DbKoukikoureiPrim.updateKoukikourei(update)
        })
        .sequence
      enterResult <- DbShahokokuhoPrim.enterShahokokuho(hoken)
      (entered, enterEvent) = enterResult
    yield (entered, List(enterEvent) ++ shahokokuhoEvents ++ koukikoureiEvents)

  def newKoukikourei(
      hoken: Koukikourei
  ): ConnectionIO[(Koukikourei, List[AppEvent])] =
    val patientId = hoken.patientId
    val startDate: LocalDate = hoken.validFrom
    val endDate: LocalDate = startDate.minusDays(1);
    for
      shahokokuhoList <- DbShahokokuhoPrim.listAvailableShahokokuho(
        patientId,
        startDate
      )
      shahokokuhoEvents <- shahokokuhoList
        .map(shahokokuho => {
          val update = shahokokuho.copy(validUpto = ValidUpto(Some(endDate)))
          DbShahokokuhoPrim.updateShahokokuho(update)
        })
        .sequence
      koukikoureiList <- DbKoukikoureiPrim.listAvailableKoukikourei(
        patientId,
        startDate
      )
      koukikoureiEvents <- koukikoureiList
        .map(koukikourei => {
          val update = koukikourei.copy(validUpto = ValidUpto(Some(endDate)))
          DbKoukikoureiPrim.updateKoukikourei(update)
        })
        .sequence
      enterResult <- DbKoukikoureiPrim.enterKoukikourei(hoken)
      (entered, enterEvent) = enterResult
    yield (entered, List(enterEvent) ++ shahokokuhoEvents ++ koukikoureiEvents)

  def batchEnterOrUpdateHoken(
      shahokokuhoList: List[Shahokokuho],
      koukikoureiList: List[Koukikourei]
  ): ConnectionIO[(List[Shahokokuho], List[Koukikourei], List[AppEvent])] =
    for
      r1 <- shahokokuhoList
        .map(shahokokuho =>
          DbShahokokuhoPrim.enterOrUpdateShahokokuho(shahokokuho)
        )
        .sequence
      shahokokuhoDb = r1.map(_._1)
      shahokokuhoEvents = r1.map(_._2)
      r2 <- koukikoureiList
        .map(koukikourei =>
          DbKoukikoureiPrim.enterOrUpdateKoukikourei(koukikourei)
        )
        .sequence
      koukikoureiDb = r2.map(_._1)
      koukikoureiEvents = r2.map(_._2)
    yield (
      shahokokuhoDb,
      koukikoureiDb,
      shahokokuhoEvents ++ koukikoureiEvents
    )

  def getHokenInfoForVisit(visitId: Int): ConnectionIO[HokenInfo] =
    for
      visit <- DbVisitPrim.getVisit(visitId).unique
      shahokokuhoOpt <- if visit.shahokokuhoId === 0 then
        None.pure[ConnectionIO] else DbShahokokuhoPrim
          .getShahokokuho(visit.shahokokuhoId)
          .unique
          .map(Some.apply)
      koukikoureiOpt <- if visit.koukikoureiId === 0 then
        None.pure[ConnectionIO] else DbKoukikoureiPrim
          .getKoukikourei(visit.koukikoureiId)
          .unique
          .map(Some.apply)
      kouhiList <- List(visit.kouhi1Id, visit.kouhi2Id, visit.kouhi3Id)
        .filter(_ > 0)
        .map(DbKouhiPrim.getKouhi(_).unique)
        .sequence
    yield HokenInfo(shahokokuhoOpt, None, koukikoureiOpt, kouhiList)
    
