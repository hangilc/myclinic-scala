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
    searchResult.ele.hide
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
  //ui.searchResult.formatter = formatPatient _

  def focus(): Unit = ui.eSearchInput.focus()

  private def onSelect(patient: Patient): Unit =
    ui.searchResult.clear()
    ui.searchResult.ele.hide
    onSelectCallback(patient)

  def hideResult: Unit =
    ui.searchResult.ele.hide

  private def onSearch(): Unit =
    val txt = ui.eSearchInput.value.trim
    val searchResult = ui.searchResult
    val formatter = (formatPatient _)
    if !txt.isEmpty then
      ui.eSearchInput.value = ""
      for (gen, patients) <- Api.searchPatient(txt)
      yield
        if patients.size == 1 then onSelect(patients.head)
        else if patients.size == 0 then ()
        else
          searchResult.clear()
          searchResult.addAll(patients, formatter)
          searchResult.ele.show
          searchResult.ele.scrollToTop

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())
