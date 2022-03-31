package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient
import org.scalajs.dom.{HTMLInputElement, HTMLElement}
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._


object PatientSearch:
  class UI:
    val eSearchInput = inputText
    val eSearchForm = form
    val eSearchButton = button
    val searchResult = Selection[Patient]()
    searchResult.hide()
    val ele = div(
      div(cls := "search-area")(
        h2("患者選択"),
        eSearchForm(
          eSearchInput,
          eSearchButton(attr("type") := "submit")("検索")
        )
      ),
      div(searchResult.ele(cls := "search-result"))
    )

class PatientSearch(ui: PatientSearch.UI):
  var onSelectCallback: Patient => Unit = _ => ()
  ui.eSearchForm(onsubmit := (onSearch _))
  ui.searchResult.formatter = formatPatient _

  def focus(): Unit = ui.eSearchInput.focus()

  def hideResult: Unit =
    ui.searchResult.hide()

  private def onSelect(patient: Patient): Unit =
    onSelectCallback(patient)

  private def onSearch(): Unit =
    val txt = ui.eSearchInput.value.trim
    val searchResult = ui.searchResult
    if !txt.isEmpty then
      ui.eSearchInput.value = ""
      for (gen, patients) <- Api.searchPatient(txt)
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
    ui.searchResult.add(patient)

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

