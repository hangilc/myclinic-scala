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

case class WqueueRow(wqueue: Wqueue, visit: Visit, patient: Patient)(using
    DataId[Wqueue],
    ModelSymbol[Wqueue]
):
  val stateLabelCell: HTMLElement = Table.cell
  val patientIdCell: HTMLElement = Table.cell
  val nameCell: HTMLElement = Table.cell
  val yomiCell: HTMLElement = Table.cell
  val sexCell: HTMLElement = Table.cell
  val birthdayCell: HTMLElement = Table.cell
  val ageCell: HTMLElement = Table.cell
  val manageCell: HTMLElement = Table.cell
  val ele = Table.createRow(
    List(
      stateLabelCell(cls := "cell-state"),
      patientIdCell(cls := "cell-patient-id"),
      nameCell(cls := "cell-name"),
      yomiCell(cls := "cell-yomi"),
      sexCell(cls := "cell-sex"),
      ageCell(cls := "cell-age"),
      birthdayCell(cls := "cell-birthday"),
      manageCell(cls := "cell-manip")
    )
  )
  ele(cls := "reception-cashier-wqueue-table-row")
  updateUI()

  private def addCashierButton(): Unit =
    manageCell(button("会計", onclick := (doCashier _)))

  private def doCashier(): Unit =
    for
      visitEx <- Api.getVisitEx(visit.visitId)
      meisai <- Api.getMeisai(visit.visitId)
    yield
      val dlog = CashierDialog(meisai, visitEx)
      dlog.open()

  def updateManageCell(): Unit =
    import WaitState.*
    manageCell(clear)
    wqueue.waitState match {
      case WaitExam | WaitReExam => ()
      case WaitCashier => 
        addCashierButton()
      case _ => ()
    }

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
