package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.apputil.ModelExt.*
import java.time.LocalDate

class PatientSelect(ui: PatientSelect.UI, onSelectCallback: Patient => Unit):
  val m = Modal("患者選択", ui.body, ui.commands)

  var selected: Option[Patient] = None
  val result = ui.selection
  result.formatter = (formatPatient _)
  result.onSelect = patient =>
    selected = Some(patient)
    ui.eSelectButton.enable()
  ui.eSelectButton(onclick := (onSelectClick _))
  ui.eCancelButton(onclick := (() => m.close()))
  ui.eSearchForm(onsubmit := (onSearch _))
  ui.eTodaysPatientsLink(onclick := (onTodaysPatients _))

  def open(): Unit = m.open()

  def close(): Unit = m.close()

  def initFocus(): Unit = ui.initFocus()

  private def onSelectClick(): Unit =
    m.close()
    onSelectCallback(selected.get)

  private def onSearch(): Unit =
    val text = ui.eInputText.value.trim
    if !text.isEmpty then
      for
        (gen, patients) <- Api.searchPatient(text)
      yield 
        result.clear()
        result.addAll(patients, formatPatient _, identity)

  private def onTodaysPatients(): Unit =
    for
      visits <- Api.listVisitByDate(LocalDate.now())
      patientMap <- Api.batchGetPatient(visits.map(_.patientId))
    yield
      val patients = visits.map(visit => patientMap(visit.patientId))
      result.clear()
      result.addAll(patients, formatPatient _, identity)

  private def formatPatient(patient: Patient): String =
    String.format("%04d %s", patient.patientId, patient.fullName())


object PatientSelect:
  class UI:
    val eSearchForm = form
    val eInputText = inputText
    val eSearchButton = button
    val eTodaysPatientsLink = a
    val selection = new Selection[Patient, Patient](identity)
    val eSelectButton = button
    val eCancelButton = button
    val body = div(
      eSearchForm(mb := "10px")(
        eInputText,
        eSearchButton("検索", ml := "5px", attr("type") := "default"),
        eTodaysPatientsLink("本日の受診", ml := "10px")
      ),
      selection.ele(
        maxWidth := "300px",
        maxHeight := "300px",
        overflowX := "auto",
        overflowY := "auto"
      )
    )
    val commands = div(
      eSelectButton("選択", disabled := true),
      eCancelButton("キャンセル")
    )

    def initFocus(): Unit = eInputText.focus()

  def open(onSelectCallback: Patient => Unit): Unit =
    val selector = new PatientSelect(new PatientSelect.UI(), onSelectCallback)
    selector.open()
    selector.initFocus()
    


