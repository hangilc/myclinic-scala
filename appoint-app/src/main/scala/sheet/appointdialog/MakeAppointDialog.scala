package dev.myclinic.scala.web.appoint.sheet.appointdialog

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.{Icons, Colors, LocalModal, ErrorBox, Modal}
import dev.myclinic.scala.model.{AppointTime, Appoint, Patient}
import dev.myclinic.scala.util.KanjiDate
import dev.fujiwara.domq.Form
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.raw.MouseEvent
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

class MakeAppointDialog(appointTime: AppointTime):
  val ui = MakeAppointUI(appointTime, () => close())
  val dlog = Modal(
    "診察予約入力",
    ui.body(cls := "appoint-dialog-body"),
    ui.commands
  )
  ui.cancelButton(onclick := (() => dlog.close()))

  def open(): Unit = 
    dlog.open()
    ui.nameInput.focus()

  def close(): Unit = dlog.close()


object MakeAppointDialogOrig:
  def open(appointTime: AppointTime): Unit =
    val ui = new UI(appointTime)
    val dlog = Modal(
      "診察予約入力",
      ui.body(cls := "make-appoint-dialog-body"),
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
    private val errBox = ErrorBox()
    body(cls := "make-appoint-dialog-body")(
      div(dateTimeRep(appointTime)),
      errBox.ele,
      Form.rows(
        span("患者名：") -> form(
          displayInlineBlock,
          onsubmit := (doSearchPatient _)
        )(
          div(cls := "input-group")(
            nameInput(placeholder := "姓　名", cls := "name-input", adjustForFlex),
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
          form(displayInlineBlock, onsubmit := (doSearchByPatientId _))(
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
      )(cls := "make-appoint-form-table")
    )
    commands(
      enterButton,
      cancelButton
    )

    def setup(close: () => Unit): Unit =
      cancelButton(onclick := close)
      enterButton(onclick := (() => doEnter(close)))

    def doSearchByPatientId(): Unit = {
      try
        val patientId = patientIdInput.value.toInt
        for patient <- Api.getPatient(patientId)
        yield {
          nameInput.value = patient.fullName(" ")
        }
      catch {
        case _: Throwable => showError("該当する患者情報をみつけられませんでした。")
      }
    }

    def doEnter(close: () => Unit): Unit =
      val f = for
        appointResult <- validate()
      yield {
        appointResult match {
          case Right(appoint) => {
            Api.registerAppoint(appoint)
            close()
          }
          case Left(msg) => errBox.show(msg)
        }
      }
      f.onComplete {
        case Success(_) => ()
        case Failure(ex) => errBox.show(ex.toString)
      }

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
      errBox.show(msg)

    def validate(): Future[Either[String, Appoint]] =
      val patientIdResult = AppointValidator.validatePatientId(patientIdInput.value)
      for
        patientOption <- 
          patientIdResult match {
            case Valid(patientId) => Api.findPatient(patientId)
            case _ => Future.successful(None)
          }
      yield {
        AppointValidator.validateForEnter(
          appointTime.appointTimeId,
          nameInput.value,
          patientIdResult,
          memoInput.value,
          patientOption
        ).toEither() 
      }

    def dateTimeRep(appointTime: AppointTime): String =
      Misc.formatAppointTimeSpan(appointTime)
