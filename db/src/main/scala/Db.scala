package dev.myclinic.scala.db

import cats.*
import cats.data.*
import cats.syntax.all.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.free.ConnectionIO
import scala.util.Try
import java.time.LocalDateTime

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
    with DbShahokokuho
    with DbRoujin
    with DbKoukikourei
    with DbKouhi
    with DbIyakuhinMaster
    with DbShinryouMaster
    with DbKizaiMaster
    with DbVisitEx:

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

  def startVisit(patientId: Int, at: LocalDateTime): IO[List[AppEvent]] =
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
      yield List(visitCreatedEvent, wqCreatedEvent)
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
