package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient
import org.scalajs.dom.{HTMLInputElement, HTMLElement}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global

object PatientSearch:
  class UI:
    val eSearchInput = inputText
    val eSearchForm = form
    val eSearchButton = button
    val eSearchResult = (new Selection.UI).hide
    val ele = div(
      div(cls := "search-area")(
        h2("患者選択"),
        eSearchForm(
          eSearchInput,
          eSearchButton(attr("type") := "submit")("検索")
        )
      ),
      div(eSearchResult.ele(cls := "search-result"))
    )

class PatientSearch(ui: PatientSearch.UI):
  var onSelectCallback: Patient => Unit = _ => ()
  private val searchResult = new Selection(ui.eSearchResult, onSelect)
  ui.eSearchForm(onsubmit := (onSearch _))

  def focus(): Unit = ui.eSearchInput.focus()

  def hideResult: Unit =
    searchResult.hide()

  private def onSelect(patient: Patient): Unit =
    onSelectCallback(patient)

  private def onSearch(): Unit =
    val txt = ui.eSearchInput.value.trim
    if !txt.isEmpty then
      ui.eSearchInput.value = ""
      for patients <- Api.searchPatient(txt)
      yield
        if patients.size == 1 then
          val patient = patients.head
          searchResult.clear()
          searchResult.hide()
          onSelect(patients.head)
        else if patients.size == 0 then
          ()
        else
          searchResult.clear()
          patients.foreach(addSearchResult(_))
          searchResult.show()
          searchResult.scrollToTop

  private def addSearchResult(patient: Patient): Unit =
    searchResult.add(
      formatPatient(patient),
      patient
    )

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

// val eSearchInput: HTMLInputElement = inputText()
// val eSearchButton = button()
// val searchResult: Selection[Patient] =
//   Selection[Patient](onSelect = patient => {
//     searchResult.clear()
//     searchResult.hide()
//     onPatientSelect(patient)
//   })
// val ele = div(cls := "search-area")(
//   h2("患者選択"),
//   form(onsubmit := (onSearch _))(
//     eSearchInput,
//     eSearchButton(attr("type") := "default")("検索")
//   )
// )

// def onPatientSelect(patient: Patient): Unit = ()

// def initFocus(): Unit = eSearchInput.focus()

// def disable(): Unit = enable(false)
// def enable(): Unit = enable(true)

// def enable(flag: Boolean): Unit =
//   eSearchInput.enable(flag)
//   eSearchButton.enable(flag)

// private def onSearch(): Unit =
//   val txt = eSearchInput.value.trim
//   if !txt.isEmpty then
//     eSearchInput.value = ""
//     for patients <- Api.searchPatient(txt)
//     yield
//       if patients.size == 1 then
//         val patient = patients.head
//         searchResult.clear()
//         searchResult.hide()
//         onPatientSelect(patients.head)
//       else
//         searchResult.clear()
//         patients.foreach(addSearchResult(_))
//         searchResult.show()
//         searchResult.ele.scrollTop = 0

// def addSearchResult(patient: Patient): Unit =
//   searchResult.add(
//     formatPatient(patient),
//     patient
//   )

// private def formatPatient(patient: Patient): String =
//   String.format("(%04d) %s", patient.patientId, patient.fullName())
