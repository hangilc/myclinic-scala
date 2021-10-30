package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.{Icons, Colors, LocalModal}
import dev.myclinic.scala.model.{AppointTime, Appoint, Patient}
import dev.myclinic.scala.util.KanjiDate
import dev.fujiwara.domq.Form
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.validator.{AppointValidator, Validators}
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.raw.MouseEvent
import scala.concurrent.Future

object MakeAppointDialog:
  def open(appointTime: AppointTime): Unit =
    val ui = new UI(appointTime)
    val dlog = Modal(
      "診察予約入力",
      ui.body,
      ui.commands
    )
    ui.setup(() => dlog.close())
    dlog.open()
    ui.nameInput.focus()

  class UI(appointTime: AppointTime):
    val body = div()
    val commands = div()
    val nameInput: HTMLInputElement = inputText()
    val nameWorkSpace: HTMLElement = div()
    val patientIdInput: HTMLInputElement = inputText()
    val patientIdWorkspace: HTMLElement = div(displayNone)
    val memoInput: HTMLInputElement = inputText()
    private val enterButton = button("入力")
    private val cancelButton = button("キャンセル")
    private val errorBox: HTMLElement = div()
    body(
      div(dateTimeRep(appointTime)),
      errorBox(
        display := "none",
        cls := "error-box"
      ),
      Form.rows(
        span("患者名：") -> div(displayInlineBlock)(
          div(
            nameInput(placeholder := "姓　名"),
            Icons.search(color = "gray", size = "1.2rem")(
              css(style => style.verticalAlign = "middle"),
              ml := "0.5rem",
              cursor := "pointer",
              onclick := (doSearchPatient _)
            )
          ),
          nameWorkSpace(displayNone, overflowYAuto, maxHeight := "10rem")
        ),
        span("患者番号：") -> div(
          div(displayInlineBlock)(
            patientIdInput(css(style => {
              style.width = "4rem"
            })),
            Icons.trash(color = "gray", size = "1.2rem")(
              css(style => style.verticalAlign = "middle"),
              ml := "0.5rem",
              cursor := "pointer",
              onclick := (() => {
                patientIdInput.value = ""
                closePatientIdWorkspace()
              })
            ),
            Icons.refresh(color = "gray", size = "1.2rem")(
              css(style => style.verticalAlign = "middle"),
              ml := "0.1rem",
              cursor := "pointer",
              onclick := (syncPatientId _)
            ),
            Icons.search(color = "gray", size = "1.2rem")(
              Icons.defaultStyle,
              onclick := (() => doSearchByPatientId())
            )
          ),
          patientIdWorkspace(
            mt := "0.3rem"
          )
        ),
        span("メモ：") -> memoInput
      )
    )
    commands(
      enterButton,
      cancelButton
    )

    def setup(close: () => Unit): Unit =
      cancelButton(onclick := close)
      enterButton(onclick := (() => {
        validate() match {
          case Right(app) => doEnter(app, close)
          case Left(msg)  => showError(msg)
        }
      }))

    def doSearchByPatientId(): Unit ={
      try
        val patientId = patientIdInput.value.toInt
        for
          patient <- Api.getPatient(patientId)
        yield {
          nameInput.value = patient.fullName(" ")
        }
      catch {
        case _: Throwable => showError("該当する患者情報をみつけられませんでした。")
      }
    }

    def doEnter(appoint: Appoint, close: () => Unit): Unit =
      def action(appoint: Appoint): Unit =
        Api.registerAppoint(appoint)
        close()

      if appoint.patientId != 0 then
        for
          patientOpt <- Api.findPatient(appoint.patientId)
          _ = patientOpt match {
            case Some(patient) => {
              val validated =
                AppointValidator.validatePatientIdConsistency(appoint, patient)
              AppointValidator.toEither(validated) match {
                case Right(appoint) => action(appoint)
                case Left(msg)      => showError(msg)
              }
            }
            case None => showError("患者番号に該当する患者情報がみつかりません。")
          }
        yield ()
      else action(appoint)

    def makeNameSlot(patient: Patient): HTMLElement =
      div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
        s"(${patient.patientId}) ${patient.fullName()}"
      )

    def closeNameWorkspace(): Unit =
      nameWorkSpace.innerHTML = ""
      nameWorkSpace(displayNone)

    def populateNameWorkspace(patients: List[Patient]): Unit =
      nameWorkSpace.innerHTML = ""
      patients.foreach(patient => {
        val e = makeNameSlot(patient)
        e(onclick := (() => {
          nameInput.value = patient.fullName()
          patientIdInput.value = patient.patientId.toString
          closeNameWorkspace()
        }))
        nameWorkSpace(e)
      })
      nameWorkSpace(display := "block")

    def doSearchPatient(): Unit =
      if nameWorkSpace.style.display != "none" then closeNameWorkspace()
      else
        for
          patients <- Api.searchPatient(nameInput.value)
          _ = populateNameWorkspace(patients)
        yield ()

    def syncPatientId(): Unit =
      if patientIdWorkspace.style.display != "none" then
        closePatientIdWorkspace()
      else
        val name = nameInput.value
        if name.contains(" ") || name.contains("　") then
          for
            patients <- Api.searchPatient(name)
            _ =
              if patients.size == 1 then
                patientIdInput.value = patients(0).patientId.toString
              else if patients.size == 0 then patientIdInput.value = ""
              else populatePatientIdWorkspace(patients)
          yield ()

    case class PatientIdSlot(patient: Patient):
      val ele: HTMLElement = div(
        div(
          cursor := "pointer",
          padding := "2px 4px",
          hoverBackground("#eee")
        )(
          div(s"(${patient.patientId.toString}) ${patient.fullName()}"),
          div(KanjiDate.dateToKanji(patient.birthday) + "生")
        )
      )

    def closePatientIdWorkspace(): Unit =
      patientIdWorkspace.innerHTML = ""
      patientIdWorkspace(display := "none")

    def populatePatientIdWorkspace(patients: List[Patient]): Unit =
      patientIdWorkspace.innerHTML = ""
      patients.foreach(patient => {
        val slot = PatientIdSlot(patient)
        slot.ele(onclick := (() => {
          patientIdInput.value = patient.patientId.toString
          closePatientIdWorkspace()
        }))
        patientIdWorkspace(slot.ele)
      })
      patientIdWorkspace()
      patientIdWorkspace(display := "block")

    def showError(msg: String): Unit =
      errorBox.clear()
      errorBox(msg, display := "block")

    def validate(): Either[String, Appoint] =
      val v = AppointValidator
        .validateForEnter(
          0,
          appointTime.appointTimeId,
          nameInput.value,
          patientIdInput.value,
          memoInput.value
        )
      AppointValidator.toEither(v)

    def dateTimeRep(appointTime: AppointTime): String =
      Misc.formatAppointTimeSpan(appointTime)
