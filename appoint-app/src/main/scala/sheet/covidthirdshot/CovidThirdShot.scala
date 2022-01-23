package dev.myclinic.scala.web.appoint.sheet.covidthirdshot

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.Patient
import java.time.LocalDate

class CovidThirdShot(val ui: CovidThirdShot.UI):
  ui.eSearchFrom(onsubmit := (onSearchSubmit _))
  ui.searchResult.onSelect = onSelect _

  def onSearchSubmit(): Unit =
    val text = ui.eSearchInput.value.trim
    if !text.isEmpty then
      ui.eDisp.clear()
      for patients <- Api.searchPatient(text)
      yield
        ui.searchResult.setItems(patients, formatOption _)
        ui.eSearchInput.value = ""

  def formatOption(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

  def onSelect(patient: Patient): Unit =
    ui.searchResult.hide()
    for data <- Api.getCovid2ndShotData(patient.patientId)
    yield data match {
      case Some(age, secondShotAt, thirdShotDue) =>
        val disp = CovidThirdShot.Disp(patient, age, secondShotAt, thirdShotDue)
        ui.eDisp.setChildren(disp.ui.ele)
      case None => ui.eDisp.innerText = s"${patient.fullName()} -- No Data"
    }

object CovidThirdShot:
  def apply(): CovidThirdShot =
    val ui = new UI
    new CovidThirdShot(ui)

  class UI:
    val eSearchFrom = form
    val eSearchInput = inputText
    val searchResult = Selection[Patient]()
    val eDisp = div
    val ele = div(
      eSearchFrom(mb := "6px")(
        eSearchInput,
        button(attr("type") := "default", "検索", ml := "6px")
      ),
      searchResult.ui.ele,
      eDisp
    )

  class Disp(
      val ui: DispUI,
      patient: Patient,
      age: Int,
      secondShotAt: LocalDate,
      thirdShotDue: LocalDate
  ):
    ui.eName.innerText = s"(${patient.patientId}) ${patient.fullName()} ${age}才"
    ui.eSecondShot.innerText = s"２回目接種日：${secondShotAt}"
    ui.eThirdShot.innerText = s"３回目接種：${thirdShotDue} より"

  object Disp:
    def apply(
        patient: Patient,
        age: Int,
        secondShotAt: LocalDate,
        thirdShotDue: LocalDate
    ): Disp =
      new Disp(new DispUI, patient, age, secondShotAt, thirdShotDue)

  class DispUI:
    val eName = div
    val eSecondShot = div
    val eThirdShot = div
    val ele = div(
      eName(fontWeight := "bold"),
      eSecondShot,
      eThirdShot(fontWeight := "bold")
    )
