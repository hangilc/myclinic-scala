package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modal, Table, ErrorBox}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.{PrintDialog}
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.apputil.HokenUtil
import org.scalajs.dom.{HTMLElement}
import java.time.{LocalDateTime, LocalDate}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import dev.fujiwara.kanjidate.KanjiDate

class CashierDialog(meisai: Meisai, visit: VisitEx):
  val patient: Patient = visit.patient
  val at: LocalDate = visit.visitedAt.toLocalDate
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
    val data = ReceiptDrawerData()
    data.setPatient(patient)
    data.charge = meisai.charge
    data.visitDate = KanjiDate.dateToKanji(at, formatYoubi = _ => "")
    data.issueDate = KanjiDate.dateToKanji(LocalDate.now(), formatYoubi = _ => "")
    data.hoken = HokenUtil.hokenRep(visit)
    data.futanWari = 
      if meisai.futanWari == 10 then "" else meisai.futanWari.toString
    meisai.items.foreach(sect => {
      val ten = if sect.subtotal > 0 then sect.subtotal.toString else ""
      sect.section match {
        case MeisaiSection.ShoshinSaisin => data.shoshin = ten
        case MeisaiSection.IgakuKanri => data.kanri = ten
        case MeisaiSection.Zaitaku => data.zaitaku = ten
        case MeisaiSection.Kensa => data.kensa = ten
        case MeisaiSection.Gazou => data.gazou = ten
        case MeisaiSection.Touyaku => data.touyaku = ten
        case MeisaiSection.Chuusha => data.chuusha = ten
        case MeisaiSection.Shochi => data.shochi = ten
        case MeisaiSection.Sonota => data.sonota = ten
      }
    })
    data.souten = meisai.totalTen.toString
    for 
      ops <- Api.drawReceipt(data)
    yield {
      CashierLib.openPrintDialog("領収書印刷", ops, modal.zIndex + 2)
      // val scale = 3
      // val w = 148
      // val h = 105
      // val settingNames = List("手動", "処方箋", "会計")
      // val dlog = PrintDialog(
      //   "領収書印刷",
      //   ops,
      //   w * scale,
      //   h * scale, 
      //   s"0, 0, $w, $h",
      //   prefKind = "receipt",
      //   zIndex = modal.zIndex + 2
      // )
      // dlog.open()
    }

  def doFinishCashier(): Unit =
    val payment = Payment(visit.visitId, meisai.charge, LocalDateTime.now())
    Api.finishCashier(payment).onComplete {
      case Success(_)  => modal.close()
      case Failure(ex) => errBox.show(ex.getMessage)
    }

  private def patientRep: String =
    s"(${patient.patientId}) ${patient.fullName()}（${patient.fullNameYomi()})"
  private def summaryLine: String =
    s"総点：${meisai.totalTen}点、負担割：${meisai.futanWari}割"
  private def chargeLine: String = s"請求額：${meisai.charge}円"
