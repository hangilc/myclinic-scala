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
import dev.fujiwara.domq.ResourceCleanups
import dev.myclinic.scala.web.reception.ReceptionBus
import dev.myclinic.scala.web.reception.scan.PatientSearch

case class WqueueRow(var wqueue: Wqueue, visit: Visit, var patient: Patient)(using
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

  val unsubs = List(ReceptionBus.wqueueUpdatedPublisher.subscribe(onWqueueUpdated),
  ReceptionBus.patientUpdatedPublisher.subscribe(onPatientUpdated))

  private def onWqueueUpdated(updated: Wqueue): Unit =
    if updated.visitId == wqueue.visitId then
      wqueue = updated
      updateStateLabel()
      updateManageCell()

  private def onPatientUpdated(updated: Patient): Unit =
    if updated.patientId == patient.patientId then
      patient = updated
      updateUI()

  private def addCashierButton(): Unit =
    manageCell(button("会計", onclick := (doCashier _)))

  private def addDeleteLink(): Unit =
    manageCell(a("削除", onclick := (doDelete _)))

  private def addRecordsLink(): Unit =
    manageCell(a("診療録", onclick := (doRecords _)))

  private def addMenuPullDown(): Unit =
    val icon = Icons.menuAlt1
    val commands: List[(String, () => Unit)] = List(
      "患者" -> doPatient,
      "削除" -> doDelete
    )
    PullDown.attachPullDown(icon, commands)
    manageCell(icon(cls := "domq-cursor-pointer"))

  private def doCashier(): Unit =
    for
      visitEx <- Api.getVisitEx(visit.visitId)
      meisai <- Api.getMeisai(visit.visitId)
    yield
      val dlog = CashierDialog(meisai, visitEx)
      dlog.open()

  private def doDelete(): Unit =
    val name = s"(${patient.patientId}) ${patient.lastName}${patient.firstName}"
    ShowMessage.confirm(s"${name}\n本当に、この受付を削除していいですか？")(deleteVisit)

  private def doPatient(): Unit =
    val dlog = new PatientSearchResultDialog(List(patient))
    dlog.open()

  private def deleteVisit(): Unit =
    (for
      _ <- Api.deleteVisit(wqueue.visitId)
    yield ()).onComplete {
      case Success (_) => ()
      case Failure(ex) => ShowMessage.showError("この受付を削除できませんでした。")
    }

  private def doRecords(): Unit =
    val dlog = new RecordDialog(patient)
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
    addRecordsLink()
    addMenuPullDown()
    // addPatientLink()
    // addDeleteLink()

  private def updateStateLabel(): Unit =
    stateLabelCell(innerText := wqueue.waitState.label)

  def updateUI(): Unit =
    updateStateLabel()
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
  given Dispose[WqueueRow] = row => 
    row.unsubs.foreach(_.proc())
