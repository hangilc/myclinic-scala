package dev.myclinic.scala.web.practiceapp.practice.record.payment

import dev.myclinic.scala.model.*
import dev.myclinic.scala.util.NumberUtil

object PaymentHelper:
  def chargeRep(chargeOption: Option[Charge]): String =
    chargeOption match {
      case None => "（未請求）"
      case Some(charge) => 
        val amount = NumberUtil.withComma(charge.charge)
        s"請求額：${amount}円"
    }

