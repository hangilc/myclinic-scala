package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modal, Table, ErrorBox}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import org.scalajs.dom.raw.{HTMLElement}
import java.time.LocalDateTime
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.scalajs.js
import scala.scalajs.js.annotation.*

@js.native
@JSGlobalScope
object DrawerSVG extends js.Object:
  def drawerJsonToSvg(
      opsJson: String,
      width: Double,
      height: Double,
      viewBox: String
  ): HTMLElement = js.native

class CashierDialog(meisai: Meisai, patient: Patient, visitId: Int):
  val table = Table()
  table.setColumns(
    List(
      e => e(width := "*"),
      e => e(cls := "right-column")
    )
  )
  meisai.items.foreach(item => {
    table.addRow(
      List(
        e => e(span(cls := "section-title")(item.section.label)),
        e => e(span(""))
      )
    )
    item.entries.foreach(entry => {
      table.addRow(
        List(
          e => e(span(entry.label)),
          e => e(span(s"${entry.tanka} x ${entry.count} = ${entry.total}"))
        )
      )
    })
  })
  val errBox = ErrorBox()
  val svgTest = div()
  val modal: Modal = Modal(
    "会計",
    div(cls := "cashier-dialog")(
      div(cls := "patient-rep")(patientRep),
      table.ele(cls := "detail-table"),
      div(
        div(summaryLine),
        div(cls := "charge-line")(chargeLine)
      ),
      errBox.ele,
      svgTest
    ),
    div(
      button("領収書印刷", onclick := (doPrintReceipt _)),
      button("会計終了", onclick := (doFinishCashier _)),
      button("キャンセル", onclick := (() => modal.close()))
    )
  )
  def open(): Unit =
    modal.open()

  def doPrintReceipt(): Unit =
    val svg = DrawerSVG.drawerJsonToSvg("""
      []
    """, 148, 105, "0, 0, 148, 105");
    svgTest(svg);

  def doFinishCashier(): Unit =
    val payment = Payment(visitId, meisai.charge, LocalDateTime.now())
    Api.finishCashier(payment).onComplete {
      case Success(_)  => modal.close()
      case Failure(ex) => errBox.show(ex.getMessage)
    }

  private def patientRep: String =
    s"(${patient.patientId}) ${patient.fullName()}（${patient.fullNameYomi()})"
  private def summaryLine: String =
    s"総点：${meisai.totalTen}点、負担割：${meisai.futanWari}割"
  private def chargeLine: String = s"請求額：${meisai.charge}円"
