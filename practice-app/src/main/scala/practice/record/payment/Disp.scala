package dev.myclinic.scala.web.practiceapp.practice.record.payment

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import scala.language.implicitConversions

case class Disp(chargeOption: Option[Charge], paymentOption: Option[Payment], visitId: Int):
  val cssPre = "practice-record-payment-disp"
  val chargeSpan = span
  val statusSpan = span
  val status: PaymentStatus = PaymentStatus(chargeOption, paymentOption)
  val ele = div(cls := cssPre,
    chargeSpan, statusSpan
  )
  chargeSpan(innerText := PaymentHelper.chargeRep(chargeOption))
  setPaymentStatus(status)
  if status != PaymentStatus.NotYet then
    ele(onclick := (doEdit _), cls := "can-edit")

  def setPaymentStatus(status: PaymentStatus): Unit =
    statusSpan(innerText := status.rep)
    statusSpan(cls :- PaymentStatus.clsList)
    statusSpan(cls := status.cls)

  def doEdit(): Unit =
    for
      meisai <- Api.getMeisai(visitId)
    yield
      ele.replaceBy(Edit(chargeOption, paymentOption, visitId, meisai).ele)


