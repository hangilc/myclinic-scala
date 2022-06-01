package dev.myclinic.scala.web.practiceapp.practice.record.payment

import dev.myclinic.scala.model.*

enum PaymentStatus(val rep: String, val cls: String = ""):
  case NotYet extends PaymentStatus("")
  case Done extends PaymentStatus("")
  case NoPayment extends PaymentStatus("（未収）", "practice-no-payment")
  case UnderPayment extends PaymentStatus("領収額不足", "practice-under-payment")
  case OverPayment extends PaymentStatus("領収額超過", "practice-over-payment")

object PaymentStatus:
  lazy val clsList: String = PaymentStatus.values.map(_.cls).toSet.mkString(" ")

  def apply(chargeOption: Option[Charge], lastPayment: Option[Payment]): PaymentStatus =
    (chargeOption, lastPayment) match {
      case (None, _) => NotYet
      case (Some(_), None) => NoPayment
      case (Some(charge), Some(payment)) if charge.charge > payment.amount => UnderPayment
      case (Some(charge), Some(payment)) if charge.charge < payment.amount => OverPayment
      case _ => Done
    }

