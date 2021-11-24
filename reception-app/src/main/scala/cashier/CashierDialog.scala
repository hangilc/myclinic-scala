package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modal, Table, ErrorBox}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.{PrintDialog}
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.raw.{HTMLElement}
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

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
  val modal: Modal = Modal(
    "会計",
    div(cls := "cashier-dialog")(
      div(cls := "patient-rep")(patientRep),
      table.ele(cls := "detail-table"),
      div(
        div(summaryLine),
        div(cls := "charge-line")(chargeLine)
      ),
      errBox.ele
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
    for opsJson <- Api.drawReceipt()
    yield {
      //val svg = DrawerSVG.drawerJsonToSvg(opsJson, 148, 105, "0, 0, 148, 105")
      val scale = 3
      val w = 148
      val h = 105
      val settingNames = List("手動", "処方箋", "会計")
      val dlog = PrintDialog(
        "領収書印刷",
        opsJson,
        w * scale,
        h * scale, 
        s"0, 0, $w, $h",
        settingNames = settingNames,
        zIndex = modal.zIndex + 2
      )
      dlog.open()
    }

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
