package dev.myclinic.scala.web.appoint.sheet.covidthirdshot

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.Patient
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import scala.util.{Try, Success, Failure}

class CovidThirdShot(val ui: CovidThirdShot.UI):
  ui.eSearchFrom(onsubmit := (onSearchSubmit _))
  ui.searchResult.addSelectEventHandler(onSelect _)

  def initFocus(): Unit = ui.eSearchInput.focus()

  def onSearchSubmit(): Unit =
    val text = ui.eSearchInput.value.trim
    if !text.isEmpty then
      ui.eDisp(clear)
      for (gen, patients) <- Api.searchPatient(text)
      yield
        if patients.size == 1 then
          onSelect(patients.head)
        else
          ui.searchResult.clear()
          ui.searchResult.formatter = (formatOption _)
          ui.searchResult.addAll(patients)
          ui.searchResult.show()
          ui.eSearchInput.value = ""

  def formatOption(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

  def onSelect(patient: Patient): Unit =
    ui.searchResult.hide()
    for data <- Api.getCovid2ndShotData(patient.patientId)
    yield data match {
      case Some(age, secondShotAt, _thirdShotDue) =>
        val due = CovidThirdShot.thirdShotDue(age, secondShotAt)
        val disp = CovidThirdShot.Disp(patient, age, secondShotAt, due)
        ui.eDisp(clear, disp.ui.ele)
      case None =>
        val query = CovidThirdShot.Query(patient)
        ui.eDisp(clear, query.ui.ele)
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
      searchResult.ele,
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

  def thirdShotDue20220120(age: Int, secondShot: LocalDate): LocalDate =
    val march = LocalDate.of(2022, 3, 1)
    if age >= 65 then
      val d: LocalDate = secondShot.plusMonths(7)
      if d.getMonthValue <= 2 then d
      else 
        val dd = secondShot.plusMonths(6)
        if dd.isBefore(march) then march else dd
    else
      val d: LocalDate = secondShot.plusMonths(8)
      if d.getMonthValue <= 2 then d
      else 
        val dd = secondShot.plusMonths(7)
        if dd.isBefore(march) then march else dd

  def thirdShotDue(age: Int, secondShot: LocalDate): LocalDate =
    secondShot.plusMonths(6)

  class Query(val ui: QueryUI, patient: Patient):
    val age = DateUtil.calcAge(patient.birthday, LocalDate.of(2022, 3, 31))
    ui.eName.innerText = s"(${patient.patientId}) ${patient.fullName()} ${age}才"
    ui.eSecondShotForm(onsubmit := (onSubmit _))

    def onSubmit(): Unit =
      ui.errorBox.hide()
      ui.eThirdShot.innerText = ""
      Try(LocalDate.parse("2021-" + ui.eSecondShotInput.value)) match {
        case Success(date) =>
          val due = thirdShotDue(age, date)
          ui.eThirdShot.innerText = s"３回目接種：${due} より"
        case Failure(_) => ui.errorBox.show("日付の入力が不適切です。\n02-18 のように入力してください。")
      }


  object Query:
    def apply(patient: Patient): Query =
      new Query(new QueryUI, patient)

  class QueryUI:
    val eName = div
    val eSecondShotForm = form
    val eSecondShotInput = inputText
    val eThirdShot = div
    val errorBox = ErrorBox()
    val ele = div(
      eName(fontWeight := "bold"),
      eSecondShotForm(
        "２回目接種日：2021-",
        eSecondShotInput(placeholder := "MM-DD"),
        button(attr("type") := "default", "入力", ml := "6px")
      ),
      errorBox.ele,
      eThirdShot(fontWeight := "bold")
    )
