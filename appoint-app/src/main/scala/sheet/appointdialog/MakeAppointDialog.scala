package dev.myclinic.scala.web.appoint.sheet.appointdialog

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{AppointTime, Appoint, Patient}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appoint.Misc
import org.scalajs.dom.Event
import scala.scalajs.js
import scala.util.{Success, Failure}
import dev.myclinic.scala.validator.AppointValidator
import scala.concurrent.Future

class MakeAppointDialog(
    ui: MakeAppointDialog.UI,
    appointTime: AppointTime,
    followingVacantRegular: () => Option[AppointTime]
):
  ui.appointTimeDisp.innerText = Misc.formatAppointTimeSpan(appointTime)
  var patientId = 0
  var patientOption: Option[Patient] = None
  val dlog = Modal(
    "診察予約入力",
    ui.body(cls := "appoint-dialog-body"),
    ui.commands
  )
  ui.cancelButton(onclick := (() => dlog.close()))
  val clearPatientId: js.Function1[Event, Unit] = _ =>
    patientId = 0
    patientOption = None
    ui.patientIdDisp(innerText := "")
    ui.nameValue.ui.input(oninput :- clearPatientId)
  ui.nameValue.onSelect = patient =>
    patientId = patient.patientId
    patientOption = Some(patient)
    ui.patientIdDisp(innerText := patient.patientId.toString)
    ui.nameValue.ui.input.value = patient.fullName()
    ui.nameValue.ui.input(oninput := clearPatientId)
  ui.enterButton(onclick := (onEnter _))
  if followingVacantRegular().isDefined then
    ui.alsoWrapper(displayDefault)
    ui.kenshinCheck(onchange := (_ => {
      ui.alsoCheck(disabled := !ui.kenshinCheck.checked)
    }))
  else ui.alsoWrapper(displayNone)

  def open(): Unit =
    dlog.open()
    ui.initFocus()

  def close(): Unit = dlog.close()

  def listTags(): Set[String] =
    Set.empty ++
      (if ui.kenshinCheck.checked then Set("健診") else Set.empty)

  def onEnter(): Unit =
    ui.errBox.hide()
    validate() match {
      case Right(appoint) =>
        for
          _ <- Api.registerAppoint(appoint)
          _ <-
            if ui.alsoCheck.checked then
              val following = followingVacantRegular().get
              val newAppoint = Appoint(
                0,
                following.appointTimeId,
                appoint.patientName,
                appoint.patientId,
                ""
              )
              Api.registerAppoint(newAppoint)
            else Future.successful(())
        yield dlog.close()
      case Left(msg) => ui.errBox.show(msg)
    }

  def validate(): Either[String, Appoint] =
    AppointValidator
      .validateForEnter(
        appointTime.appointTimeId,
        ui.nameValue.value,
        AppointValidator.validatePatientIdValue(patientId),
        ui.memoInput.value,
        listTags(),
        patientOption
      )
      .asEither

object MakeAppointDialog:
  def apply(
      appointTime: AppointTime,
      followingVacantRegular: () => Option[AppointTime]
  ): MakeAppointDialog =
    new MakeAppointDialog(new UI, appointTime, followingVacantRegular)

  class UI:
    val appointTimeDisp = div
    val nameValue = NameValue()
    val patientIdDisp = span
    val memoInput = inputText
    val kenshinCheck = checkbox()
    val alsoWrapper = span()
    val alsoCheck = checkbox()
    val errBox = ErrorBox()
    val enterButton = button
    val cancelButton = button
    val body = div(
      appointTimeDisp,
      Form.rows(
        span("患者名：") -> nameValue.ui.ele,
        span("患者番号：") -> patientIdDisp,
        span("メモ：") -> memoInput,
        span("タグ：") -> div(
          kenshinCheck,
          "健診",
          alsoWrapper(
            alsoCheck(disabled := true),
            label("診察も")
          )
        )
      ),
      errBox.ele
    )
    val commands = div(
      enterButton("入力"),
      cancelButton("キャンセル")
    )
    def initFocus(): Unit = nameValue.initFocus()

  class NameValue(val ui: NameValue.UI):
    var onSelect: Patient => Unit = _ => ()
    ui.searchForm(onsubmit := (onSubmit _))
    ui.searchIcon(onclick := (onSubmit _))
    ui.searchResult.onSelect = patient =>
      ui.input.value = patient.fullName()
      ui.hideSearchResult()
      onSelect(patient)

    def onSubmit(): Unit =
      val txt = value
      if !txt.isEmpty then
        for (gen, patients) <- Api.searchPatient(txt)
        yield
          if patients.size == 1 then onSelect(patients.head)
          else
            ui.searchResult.clear()
            ui.searchResult.formatter = patient =>
              s"(${patient.patientId}) ${patient.fullName()}"
            ui.searchResult.addAll(patients)
            ui.showSearchResult()

    def value: String = ui.input.value
    def initFocus(): Unit = ui.input.focus()

  object NameValue:
    def apply(): NameValue =
      new NameValue(new UI)
    class UI:
      val inputArea = div
      val workArea = div
      val searchForm = form
      val input = inputText(width := "100%")
      val searchIcon = Icons.search
      val searchResult = Selection[Patient]()
      private val searchResultWrapper = div(displayNone)
      searchResult.hide()
      val ele = div(
        inputArea(Form.inputGroup)(
          searchForm(input, flex := "1 1 auto"),
          searchIcon(Icons.defaultStyle, ml := "0.5rem")
        ),
        searchResultWrapper(searchResult.ele(mt := "6px"))
      )
      def showSearchResult(): Unit =
        searchResult.show()
        searchResultWrapper(displayDefault)
      def hideSearchResult(): Unit =
        searchResult.hide()
        searchResultWrapper(displayNone)
