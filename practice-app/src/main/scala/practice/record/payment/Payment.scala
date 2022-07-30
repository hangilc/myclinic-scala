package dev.myclinic.scala.web.practiceapp.practice.record.payment

import dev.myclinic.scala.web.practiceapp.practice.record.payment.Disp
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Payment as ModelPayment, *}
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus

case class Payment(
    var chargeOption: Option[Charge],
    var paymentOption: Option[ModelPayment],
    visitId: Int
):
  val unsubs = List(
    PracticeBus.paymentEntered.subscribe(onPaymentEntered _)
  )
  val ele = div(cls := "practice-record-payment")
  disp()

  def disp(): Unit =
    ele(clear, Disp(chargeOption, paymentOption, visitId).ele)

  def onChargeUpdated(newCharge: Charge): Unit =
    chargeOption = Some(newCharge)
    disp()

  def onPaymentEntered(payment: ModelPayment): Unit =
    if payment.visitId == visitId then
      println(("onPaymentEntered", payment))
      paymentOption = Some(payment)
      disp()

object Payment:
  given Dispose[Payment] = Dispose.nop[Payment] + (_.unsubs)

