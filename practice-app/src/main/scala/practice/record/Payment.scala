package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.web.practiceapp.practice.record.payment.Disp
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Payment as ModelPayment, *}

case class Payment(
    var chargeOption: Option[Charge],
    var paymentOption: Option[ModelPayment],
    visitId: Int
):
  val ele = div(cls := "practice-record-payment")
  disp()

  def disp(): Unit =
    ele(clear, Disp(chargeOption, paymentOption, visitId).ele)

  def onChargeUpdated(newCharge: Charge): Unit =
    chargeOption = Some(newCharge)
    disp()

