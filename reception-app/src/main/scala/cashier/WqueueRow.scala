package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.DataSource
import org.scalajs.dom.HTMLElement
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.DateUtil
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Failure
import scala.util.Success

case class WqueueRow(wqueue: Wqueue, visit: Visit, patient: Patient)(using
    DataId[Wqueue],
    ModelSymbol[Wqueue]
):
  val stateLabelCell: HTMLElement = div
  val patientIdCell: HTMLElement = div
  val nameCell: HTMLElement = div
  val yomiCell: HTMLElement = div
  val sexCell: HTMLElement = div
  val birthdayCell: HTMLElement = div
  val ageCell: HTMLElement = div
  val manageCell: HTMLElement = div
  val ele = Table.createRow(
    List(
      Table.cell(cls := "cell-state cell", stateLabelCell(cls := "content")),
      Table.cell(cls := "cell-patient-id cell", patientIdCell(cls := "content")),
      Table.cell(cls := "cell-name cell", nameCell(cls := "content")),
      Table.cell(cls := "cell-yomi cell", yomiCell(cls := "content")),
      Table.cell(cls := "cell-sex cell", sexCell(cls := "content")),
      Table.cell(cls := "cell-age cell", ageCell(cls := "content")),
      Table.cell(cls := "cell-birthday cell", birthdayCell(cls := "content")),
      Table.cell(cls := "cell-manip cell", manageCell(cls := "content"))
    )
  )
  ele(cls := "reception-cashier-wqueue-table-row")
  updateUI()

  private def addCashierButton(): Unit =
    manageCell(button("会計", onclick := (doCashier _)))

  private def addDeleteLink(): Unit =
    manageCell(a("削除", onclick := (doDelete _)))

  private def doCashier(): Unit =
    for
      visitEx <- Api.getVisitEx(visit.visitId)
      meisai <- Api.getMeisai(visit.visitId)
    yield
      val dlog = CashierDialog(meisai, visitEx)
      dlog.open()

  private def doDelete(): Unit =
    (for
      _ <- Api.deleteVisit(wqueue.visitId)
    yield ()).onComplete {
      case Success (_) => ()
      case Failure(ex) => ShowMessage.showError("この受付を削除できませんでした。")
    }

  def updateManageCell(): Unit =
    import WaitState.*
    manageCell(clear)
    wqueue.waitState match {
      case WaitExam | WaitReExam => ()
      case WaitCashier => 
        addCashierButton()
      case _ => ()
    }
    addDeleteLink()

  def updateUI(): Unit =
    stateLabelCell(innerText := wqueue.waitState.label)
    patientIdCell(innerText := String.format("%04d", patient.patientId))
    nameCell(innerText := patient.fullName())
    yomiCell(innerText := patient.fullNameYomi())
    sexCell(innerText := patient.sex.rep)
    ageCell(innerText := age(patient.birthday).toString + "才")
    birthdayCell(innerText := birthday(patient.birthday))
    updateManageCell()

  def birthday(d: LocalDate): String = KanjiDate.dateToKanji(d)

  def age(birthday: LocalDate): Int =
    DateUtil.calcAge(birthday, LocalDate.now())

object WqueueRow:
  given Ordering[WqueueRow] = Ordering.by(_.wqueue.visitId)
  given Comp[WqueueRow] = _.ele
  given Dispose[WqueueRow] = _ => ()
