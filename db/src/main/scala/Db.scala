package dev.myclinic.scala.db

import cats.*
import cats.data.*
import cats.syntax.all.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.free.ConnectionIO
import doobie.syntax.all.*
import scala.util.Try
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.db.DbKoukikoureiPrim
import dev.myclinic.scala.db.DoobieMapping.given

object Db
    extends Mysql
    with DbAppoint
    with DbEvent
    with DbPatient
    with DbHotline
    with DbWqueue
    with DbVisit
    with DbText
    with DbDrug
    with DbShinryou
    with DbConduct
    with DbConductDrug
    with DbConductShinryou
    with DbConductKizai
    with DbCharge
    with DbPayment
    with DbHoken
    with DbShahokokuho
    with DbRoujin
    with DbKoukikourei
    with DbKouhi
    with DbIyakuhinMaster
    with DbShinryouMaster
    with DbKizaiMaster
    with DbVisitEx
    with DbDisease
    with DbByoumeiMaster
    with DbShuushokugoMaster:

  def deleteVisit(visitId: Int): IO[List[AppEvent]] =
    def check(chk: Int => IO[Int], err: String): EitherT[IO, String, Unit] =
      EitherT(chk(visitId).map(c => if c > 0 then Left(err) else Right(())))
    def proc(): IO[List[AppEvent]] =
      val op =
        for
          wqueueEventOpt <- DbWqueuePrim.tryDeleteWqueue(visitId)
          visit <- DbVisitPrim.getVisit(visitId).unique
          _ <- DbVisitPrim.deleteVisit(visitId)
          visitEvent <- DbEventPrim.logVisitDeleted(visit)
        yield wqueueEventOpt.toList :+ visitEvent
      mysql(op)
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

  def finishCashier(payment: Payment): IO[List[AppEvent]] =
    mysql(for
      paymentEvent <- DbPaymentPrim.enterPayment(payment)
      wqEventOpt <- DbWqueuePrim.tryDeleteWqueue(payment.visitId)
    yield List(paymentEvent) ++ wqEventOpt.toList)

  def startVisit(
      patientId: Int,
      at: LocalDateTime
  ): IO[(Visit, List[AppEvent])] =
    val date = at.toLocalDate
    mysql(
      for
        shahokokuhoOpt <- DbShahokokuhoPrim
          .listAvailableShahokokuho(patientId, date)
          .map(_.headOption)
        shahokokuhoId = shahokokuhoOpt.map(_.shahokokuhoId).getOrElse(0)
        koukikoureiOpt <- DbKoukikoureiPrim
          .listAvailableKoukikourei(patientId, date)
          .map(_.headOption)
        koukikoureiId = koukikoureiOpt.map(_.koukikoureiId).getOrElse(0)
        roujinOpt <- DbRoujinPrim
          .listAvailableRoujin(patientId, date)
          .map(_.headOption)
        roujinId = roujinOpt.map(_.roujinId).getOrElse(0)
        kouhiList <- DbKouhiPrim.listAvailableKouhi(patientId, date)
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
    )

  def deleteShahokokuho(shahokokuhoId: Int): IO[AppEvent] =
    mysql {
      for
        used <- DbVisitPrim.countByShahokokuho(shahokokuhoId)
        _ = if used != 0 then
          throw new RuntimeException(
            s"Cannot delete used shahokokuho: ${shahokokuhoId}."
          )
        event <- DbShahokokuhoPrim.deleteShahokokuho(shahokokuhoId)
      yield event
    }

  def deleteRoujin(roujinId: Int): IO[AppEvent] =
    mysql {
      for
        used <- DbVisitPrim.countByRoujin(roujinId)
        _ = if used != 0 then
          throw new RuntimeException(
            s"Cannot delete used roujin: ${roujinId}."
          )
        event <- DbRoujinPrim.deleteRoujin(roujinId)
      yield event
    }

  def deleteKoukikourei(koukikoureiId: Int): IO[AppEvent] =
    mysql {
      for
        used <- DbVisitPrim.countByKoukikourei(koukikoureiId)
        _ = if used != 0 then
          throw new RuntimeException(
            s"Cannot delete used koukikourei: ${koukikoureiId}."
          )
        event <- DbKoukikoureiPrim.deleteKoukikourei(koukikoureiId)
      yield event
    }

  def deleteKouhi(kouhiId: Int): IO[AppEvent] =
    mysql {
      for
        used <- DbVisitPrim.countByKouhi(kouhiId)
        _ = if used != 0 then
          throw new RuntimeException(
            s"Cannot delete used kouhi: ${kouhiId}."
          )
        event <- DbKouhiPrim.deleteKouhi(kouhiId)
      yield event
    }

  def listWqueueFull()
      : IO[(Int, List[Wqueue], Map[Int, Visit], Map[Int, Patient])] =
    mysql {
      for
        gen <- DbEventPrim.currentEventId()
        wqueueList <- DbWqueuePrim.listWqueue().to[List]
        visitMap <- DbVisitPrim.batchGetVisit(wqueueList.map(_.visitId))
        patientMap <- DbPatientPrim.batchGetPatient(
          visitMap.values.map(_.patientId).toList
        )
      yield (gen, wqueueList, visitMap, patientMap)
    }

  def findWqueueFull(visitId: Int): IO[Option[(Int, Wqueue, Visit, Patient)]] =
    val op =
      for
        gen <- DbEventPrim.currentEventId()
        wqueueOpt <- DbWqueuePrim.getWqueue(visitId).option
        visitOpt <- wqueueOpt match {
          case Some(wqueue) =>
            DbVisitPrim.getVisit(wqueue.visitId).unique.map(v => Some(v))
          case None => None.pure[ConnectionIO]
        }
        patientOpt <- visitOpt match {
          case Some(visit) =>
            DbPatientPrim.getPatient(visit.patientId).unique.map(p => Some(p))
          case None => None.pure[ConnectionIO]
        }
      yield wqueueOpt match {
        case Some(_) => Some(gen, wqueueOpt.get, visitOpt.get, patientOpt.get)
        case None    => None
      }
    mysql(op)

  def getVisitPatient(visitId: Int): IO[(Int, Visit, Patient)] =
    mysql(for
      gen <- DbEventPrim.currentEventId()
      visit <- DbVisitPrim.getVisit(visitId).unique
      patient <- DbPatientPrim.getPatient(visit.patientId).unique
    yield (gen, visit, patient))

  def getPatientHoken(patientId: Int, at: LocalDate): IO[
    (
        Int,
        Patient,
        List[Shahokokuho],
        List[Koukikourei],
        List[Roujin],
        List[Kouhi]
    )
  ] =
    val op =
      for
        gen <- DbEventPrim.currentEventId()
        patient <- DbPatientPrim.getPatient(patientId).unique
        shahokokuho <- DbShahokokuhoPrim.listAvailableShahokokuho(patientId, at)
        koukikourei <- DbKoukikoureiPrim.listAvailableKoukikourei(patientId, at)
        roujin <- DbRoujinPrim.listAvailableRoujin(patientId, at)
        kouhi <- DbKouhiPrim.listAvailableKouhi(patientId, at)
      yield (gen, patient, shahokokuho, koukikourei, roujin, kouhi)
    mysql(op)

  def listAllHoken(patientId: Int): IO[
    (
        List[Shahokokuho],
        List[Koukikourei],
        List[Roujin],
        List[Kouhi]
    )
  ] =
    val op =
      for
        shahokokuho <- DbShahokokuhoPrim.listShahokokuho(patientId)
        koukikourei <- DbKoukikoureiPrim.listKoukikourei(patientId)
        roujin <- DbRoujinPrim.listRoujin(patientId)
        kouhi <- DbKouhiPrim.listKouhi(patientId)
      yield (shahokokuho, koukikourei, roujin, kouhi)
    mysql(op)

  def getPatientAllHoken(patientId: Int): IO[
    (
        Int,
        Patient,
        List[Shahokokuho],
        List[Koukikourei],
        List[Roujin],
        List[Kouhi]
    )
  ] =
    val op =
      for
        gen <- DbEventPrim.currentEventId()
        patient <- DbPatientPrim.getPatient(patientId).unique
        shahokokuho <- DbShahokokuhoPrim.listShahokokuho(patientId)
        koukikourei <- DbKoukikoureiPrim.listKoukikourei(patientId)
        roujin <- DbRoujinPrim.listRoujin(patientId)
        kouhi <- DbKouhiPrim.listKouhi(patientId)
      yield (gen, patient, shahokokuho, koukikourei, roujin, kouhi)
    mysql(op)

  def listRecentVisitFull(offset: Int, limit: Int): IO[List[(Visit, Patient)]] =
    val op = sql"""
      select visit.*, patient.* from visit inner join patient on visit.patient_id = patient.patient_id
      order by visit.visit_id desc limit ${limit} offset ${offset}
    """.query[(Visit, Patient)].to[List]
    mysql(op)

  def batchCreateShinryouConduct(
      req: CreateShinryouConductRequest
  ): IO[(List[AppEvent], List[Int], List[Int])] =
    mysql(DbPrim.batchEnterShinryouConduct(req))

  def listCurrentDiseaseEx(
      patientId: Int
  ): IO[List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]] =
    mysql(DbPrim.listCurrentDiseaseEx(patientId))

  def listDiseaseEx(
      patientId: Int
  ): IO[List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]] =
    mysql(DbPrim.listDiseaseEx(patientId))

  def enterDiseaseEx(
      patientId: Int,
      byoumeicode: Int,
      startDate: LocalDate,
      adjCodes: List[Int]
  ): IO[(Int, List[AppEvent])] =
    val op = for
      enterResult <- DbDiseasePrim.enterDisease(
        patientId,
        byoumeicode,
        startDate,
        ValidUpto(None),
        DiseaseEndReason.NotEnded.code
      )
      (diseaseId, enterEvent) = enterResult
      adjEvents <- adjCodes
        .map(code =>
          DbDiseaseAdjPrim.enterDiseaseAdj(diseaseId, code).map(_._2)
        )
        .sequence
    yield (diseaseId, List(enterEvent) ++ adjEvents)
    mysql(op)

  def getDiseaseEx(
      diseaseId: Int
  ): IO[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])] =
    mysql(DbPrim.getDiseaseEx(diseaseId))

  def updateDiseaseEx(
      disease: Disease,
      shuushokugocodes: List[Int]
  ): IO[List[AppEvent]] =
    val op =
      for
        updateEvent <- DbDiseasePrim.updateDisease(disease)
        curAdjList <- DbDiseaseAdjPrim
          .listDiseaseAdj(disease.diseaseId)
          .to[List]
        curAdjCodes <- curAdjList
          .map(adj =>
            DbShuushokugoMasterPrim
              .getShuushokugoMaster(adj.shuushokugocode, disease.startDate)
              .map(_.shuushokugocode)
              .unique
          )
          .sequence
        adjEvents <-
          if shuushokugocodes == curAdjCodes then
            List.empty[AppEvent].pure[ConnectionIO]
          else
            val delEvents = curAdjList.map(adj =>
              DbDiseaseAdjPrim.deleteDiseaseAdj(adj.diseaseAdjId)
            )
            val createEvents = shuushokugocodes.map(code =>
              DbDiseaseAdjPrim
                .enterDiseaseAdj(disease.diseaseId, code)
                .map(_._2)
            )
            (delEvents ++ createEvents).sequence
      yield List(updateEvent) ++ adjEvents
    mysql(op)

  def deleteDiseaseEx(diseaseId: Int): IO[List[AppEvent]] =
    val op =
      for
        adjIds <- DbDiseaseAdjPrim
          .listDiseaseAdj(diseaseId)
          .map(_.diseaseAdjId)
          .to[List]
        diseaseEvent <- DbDiseasePrim.deleteDisease(diseaseId)
        adjEvents <- adjIds
          .map(adjId => DbDiseaseAdjPrim.deleteDiseaseAdj(adjId))
          .sequence
      yield List(diseaseEvent) ++ adjEvents
    mysql(op)

  def batchCountHokenUsage(shahokokuhoIds: List[Int], koukikoureiIds: List[Int], roujinIds: List[Int], kouhiIds: List[Int]):
    IO[(Map[Int, Int], Map[Int, Int], Map[Int, Int], Map[Int, Int])] =
    val op = 
      for
        shahokokuhoCounts <- shahokokuhoIds.map(id => DbShahokokuhoPrim.countShahokokuhoUsage(id)).sequence
        koukikoureiCounts <- koukikoureiIds.map(id => DbKoukikoureiPrim.countKoukikoureiUsage(id)).sequence
        roujinCounts <- roujinIds.map(id => DbRoujinPrim.countRoujinUsage(id)).sequence
        kouhiCounts <- kouhiIds.map(id => DbKouhiPrim.countKouhiUsage(id)).sequence
      yield
        (
          Map[Int, Int](shahokokuhoIds.zip(shahokokuhoCounts): _*),
          Map[Int, Int](koukikoureiIds.zip(koukikoureiCounts): _*),
          Map[Int, Int](roujinIds.zip(roujinCounts): _*),
          Map[Int, Int](kouhiIds.zip(kouhiCounts): _*)
        )
    mysql(op)

  def batchGetChargePayment(visitIds: List[Int]): IO[Map[Int, (Option[Charge], Option[Payment])]] =
    val op =
      for
        charges <- visitIds.map(visitId => DbChargePrim.getCharge(visitId).option).sequence
        payments <- visitIds.map(visitId => DbPaymentPrim.getLastPayment(visitId).option).sequence
      yield 
        val values = charges.zip(payments)
        Map.from(visitIds.zip(values))
    mysql(op)
