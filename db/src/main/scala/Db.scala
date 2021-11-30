package dev.myclinic.scala.db

import cats.*
import cats.data.*
import cats.syntax.all.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.free.ConnectionIO
import scala.util.Try

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
    mysql(
      
    )

