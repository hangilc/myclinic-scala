package dev.myclinic.scala.web.practiceapp.practice.record.payment

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import java.time.LocalDateTime
import dev.myclinic.scala.drawerform.receipt.ReceiptDrawerData
import java.time.LocalDate

case class Edit(
    chargeOption: Option[Charge],
    paymentOption: Option[Payment],
    visitId: Int,
    meisai: Meisai
):
  val newChargeInput = input
  val ele = div(
    cls := "practice-widget",
    div(cls := "practice-widget-title", innerText := "請求額の変更"),
    div(
      div(span("診療報酬総点"), span(meisai.totalTen.toString + "点")),
      div(span("負担割"), span(meisai.futanWari.toString + "割")),
      div(span("現在の請求額", span(chargeOption.fold("0")(_.charge.toString) + "円"))),
      div(span("変更後請求額"), newChargeInput, "円")
    ),
    paymentOption.map(pay => 
      div(span("最終徴収額"), span(pay.amount.toString + "円"))
    ),
    div(
      cls := "practice-widget-commands",
      a("未収に", onclick := (doNoPayment _)),
      a("領収書PDF", onclick := (doReceiptPdf _)),
      button("入力", onclick := (doEnter _)),
      button("キャンセル", onclick := (doCancel _))
    )
  )

  def doCancel(): Unit =
    ele.replaceBy(Disp(chargeOption, paymentOption, visitId).ele)

  def doEnter(): Unit =
    newChargeInput.value.toIntOption match {
      case None => ShowMessage.showError("変更後請求額の入力が不適切です。")
      case Some(newCharge) => 
        for
          updated <- Api.updateChargeValue(visitId, newCharge)
        yield PracticeBus.chargeUpdated.publish(updated)
    }

  def doNoPayment(): Unit =
    val pay = Payment(visitId, 0, LocalDateTime.now())
    for
      _ <- Api.enterPayment(pay)
    yield
      PracticeBus.paymentEntered.publish(pay)

  def doReceiptPdf(): Unit =
    for
      visit <- Api.getVisit(visitId)
      patient <- Api.getPatient(visit.patientId)
      data = ReceiptDrawerData.create(patient, meisai, visit, LocalDate.now())
      ops <- Api.drawReceipt(data)
    yield
      Api.createPdfFile(ops, "A5_Landscape")
      
      

