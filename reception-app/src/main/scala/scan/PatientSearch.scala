package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global

abstract class PatientSearch:
  val eSearchInput: HTMLInputElement = inputText()
  val searchResult: Selection[Patient] =
    Selection[Patient](onSelect = patient => {
      searchResult.clear()
      searchResult.hide()
      onPatientSelect(patient)
    })
  val ele = div(cls := "search-area")(
    h2("患者選択"),
    form(onsubmit := (onSearch _))(
      eSearchInput,
      button(attr("type") := "default")("検索")
    )
  )

  def onPatientSelect(patient: Patient): Unit

  def initFocus(): Unit = eSearchInput.focus()

  private def onSearch(): Unit =
    val txt = eSearchInput.value.trim
    if !txt.isEmpty then
      eSearchInput.value = ""
      for patients <- Api.searchPatient(txt)
      yield
        if patients.size == 1 then
          val patient = patients.head
          searchResult.clear()
          searchResult.hide()
          onPatientSelect(patients.head)
        else
          searchResult.clear()
          patients.foreach(addSearchResult(_))
          searchResult.show()
          searchResult.ele.scrollTop = 0

  def addSearchResult(patient: Patient): Unit =
    searchResult.add(
      formatPatient(patient),
      patient
    )

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())


