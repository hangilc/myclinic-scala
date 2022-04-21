package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Payment as ModelPayment, *}
import dev.myclinic.scala.util.NumberUtil

class Payment(visit: VisitEx):
  val chargeSpan = span
  val statusSpan = span
  val ele = div(
    chargeSpan, statusSpan
  )
  chargeSpan(innerText := Payment.chargeRep(visit))
  setPaymentStatus(Payment.paymentStatus(visit))

  def setPaymentStatus(status: Payment.PaymentStatus): Unit =
    statusSpan(innerText := status.rep)
    statusSpan(cls :- Payment.PaymentStatus.clsList)
    statusSpan(cls := status.cls)

object Payment:
  def chargeRep(visit: VisitEx): String =
    visit.chargeOption match {
      case None => "（未請求）"
      case Some(charge) => 
        val amount = NumberUtil.withComma(charge.charge)
        s"請求額：${amount}円"
    }

  enum PaymentStatus(val rep: String, val cls: String = ""):
    case NotYet extends PaymentStatus("")
    case Done extends PaymentStatus("")
    case NoPayment extends PaymentStatus("（未収）", "practice-no-payment")
    case UnderPayment extends PaymentStatus("領収額不足", "practice-under-payment")
    case OverPayment extends PaymentStatus("領収額超過", "practice-over-payment")

  object PaymentStatus:
    lazy val clsList: String = PaymentStatus.values.map(_.cls).toSet.mkString(" ")

  def paymentStatus(visit: VisitEx): PaymentStatus =
    import PaymentStatus.*
    (visit.chargeOption, visit.lastPayment) match {
      case (None, _) => NotYet
      case (Some(_), None) => NoPayment
      case (Some(charge), Some(payment)) if charge.charge > payment.amount => UnderPayment
      case (Some(charge), Some(payment)) if charge.charge < payment.amount => OverPayment
      case _ => Done

    }
