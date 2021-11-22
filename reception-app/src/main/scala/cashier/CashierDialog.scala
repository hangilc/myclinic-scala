package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modal, Table}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import org.scalajs.dom.raw.{HTMLElement}

class CashierDialog(meisai: Meisai, patient: Patient):
  val table = Table()
  table.setColumns(List(
    e => e(width := "*"),
    e => e(cls := "right-column")
  ))
  meisai.items.foreach(item => {
    table.addRow(List(
      e => e(span(cls := "section-title")(item.section.label)),
      e => e(span(""))
    ))
    item.entries.foreach(entry => {
      table.addRow(List(
        e => e(span(entry.label)),
        e => e(span(s"${entry.tanka} x ${entry.count} = ${entry.total}"))
      ))
    })
  })
  val modal: Modal = Modal(
    "会計",
    div(cls := "cashier-dialog")(
      div(cls := "patient-rep")(patientRep),
      table.ele(cls := "detail-table"),
      div(
        div(summaryLine),
        div(cls := "charge-line")(chargeLine)
      )
    ),
    div(
      button("領収書印刷"),
      button("会計終了"),
      button("キャンセル", onclick := (() => modal.close()))
    )
  )
  def open(): Unit = 
    modal.open()
  private def patientRep: String =
    s"(${patient.patientId}) ${patient.fullName()}（${patient.fullNameYomi()})"
  private def summaryLine: String = 
    s"総点：${meisai.totalTen}点、負担割：${meisai.futanWari}割"
  private def chargeLine: String = s"請求額：${meisai.charge}円"