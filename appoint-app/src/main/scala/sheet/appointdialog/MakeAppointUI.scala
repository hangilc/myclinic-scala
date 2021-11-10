package dev.myclinic.scala.web.appoint.sheet.appointdialog

import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Form, Icons, ErrorBox}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{AppointTime, Patient, Appoint}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import org.scalajs.dom.raw.Event

trait MakeAppointUI:
  val body: HTMLElement
  val commands: HTMLElement
  def nameInput: HTMLInputElement
  val cancelButton: HTMLElement

object MakeAppointUI:
  def apply(
      appointTime: AppointTime,
      cb: () => Unit,
      followingVacantRegular: () => Option[AppointTime]
  ): MakeAppointUI =
    Builder(appointTime, cb, followingVacantRegular).build()

  class Builder(
      appointTime: AppointTime,
      cb: () => Unit,
      followingVacantRegular: () => Option[AppointTime]
  ):
    val errBox = ErrorBox()
    val namePart = NamePart(patient => setPatient(patient))
    val patientIdPart = PatientIdPart(patient => setPatient(patient))
    val memoPart = MemoPart()
    val tagPart = TagPart(followingVacantRegular)
    def setPatient(patient: Patient): Unit =
      namePart.input.value = patient.fullName(" ")
      patientIdPart.input.value = patient.patientId.toString
    def build(): MakeAppointUI =
      new MakeAppointUI:
        def nameInput: HTMLInputElement = namePart.input
        val enterButton = button("入力")
        val cancelButton = button("キャンセル")
        val body: HTMLElement =
          div(
            errBox.ele,
            div(Misc.formatAppointTimeSpan(appointTime)),
            Form.rows(
              span("患者名：") -> namePart.ele,
              span("患者番号：") -> patientIdPart.ele,
              span("メモ：") -> memoPart.ele,
              span("タグ：") -> tagPart.ele
            )(cls := "appoint-dialog-form-table")
          )
        val commands: HTMLElement =
          div(
            enterButton(onclick := (() => onEnter())),
            cancelButton
          )
        def onEnter(): Unit =
          val f =
            for
              appoint <- enter()
              _ <-
                val followOpt = followingVacantRegular()
                if tagPart.isFollowingChecked && followOpt.isDefined then
                  enterFollow(appoint, followOpt.get)
                else Future.successful(())
            yield ()
          f.onComplete {
            case Success(_)  => cb()
            case Failure(ex) => errBox.show(ex.getMessage)
          }
        def enter(): Future[Appoint] =
          for
            validated <- validate()
            appoint <- validated match {
              case Right(appoint) => Api.registerAppoint(appoint)
              case Left(msg)      => Future.failed(new Exception(msg))
            }
          yield appoint
        def enterFollow(
            appoint: Appoint,
            appointTime: AppointTime
        ): Future[Unit] =
          for
            patientOpt <- Api.findPatient(appoint.patientId)
            _ <- AppointValidator.validateForEnter(
              appointTime.appointTimeId,
              appoint.patientName,
              AppointValidator.validatePatientIdValue(appoint.patientId),
              "",
              Set.empty,
              patientOpt
            ).toEither() match {
              case Right(newAppoint) => Api.registerAppoint(newAppoint)
              case Left(msg) => Future.failed(new Exception(msg))
            }
          yield ()
        def validate(): Future[Either[String, Appoint]] =
          val patientIdResult =
            AppointValidator.validatePatientId(patientIdPart.input.value)
          for
            patientOption <- patientIdResult match {
              case Valid(patientId) => Api.findPatient(patientId)
              case Invalid(_)       => Future.successful(None)
            }
          yield {
            AppointValidator
              .validateForEnter(
                appointTime.appointTimeId,
                namePart.input.value,
                patientIdResult,
                memoPart.input.value,
                tagPart.tags,
                patientOption
              )
              .toEither()
          }

  class NamePart(setPatient: Patient => Unit)
      extends ValuePart
      with SearchResult:
    def updateUI(): Unit = ()
    val input: HTMLInputElement = inputText(width := "100%")
    val main: HTMLElement = Form.inputGroup(
      form(
        input,
        onsubmit := (() => doSearch()),
        flex := "1 1 auto"
      ),
      Icons.search(color = "gray", size = "1.2rem")(
        Icons.defaultStyle,
        ml := "0.5rem",
        onclick := (() => doSearch())
      )
    )
    def doSearch(): Unit =
      val text = input.value.trim
      if !text.isEmpty then
        val f =
          for patients <- Api.searchPatient(text)
          yield populateSearchResult(
            patients,
            patient => {
              setPatient(patient)
              initWorkarea()
            }
          )
        f.catchErr

  class PatientIdPart(setPatient: Patient => Unit)
      extends ValuePart
      with SearchResult:
    def updateUI(): Unit = ()
    val input: HTMLInputElement = Form.fixedSizeInput("4rem")
    val main: HTMLElement = Form.inputGroup(
      form(
        input,
        onsubmit := (() => doSearch())
      ),
      Icons.search(color = "gray", size = "1.2rem")(
        Icons.defaultStyle,
        ml := "0.5rem",
        onclick := (() => doSearch())
      )
    )
    def doSearch(): Unit =
      try
        val patientId = input.value.toInt
        val f =
          for patientOption <- Api.findPatient(patientId)
          yield {
            patientOption match {
              case Some(patient) =>
                populateSearchResult(
                  List(patient),
                  patient => {
                    setPatient(patient)
                    initWorkarea()
                  }
                )
              case None => showError("該当する患者情報がありません。")
            }
          }
        f.catchErr
      catch {
        case _: Throwable => showError("患者番号の入力が整数でありません。")
      }

  class MemoPart extends ValuePart:
    def updateUI(): Unit = ()
    val input: HTMLInputElement = Form.input
    val main: HTMLElement = Form.inputGroup(
      input
    )

  class TagPart(followingVacantRegular: () => Option[AppointTime])
      extends ValuePart:
    def updateUI(): Unit = ()
    val kenshinCheck: HTMLInputElement = checkbox()
    val alsoWrapper: HTMLElement = span()
    val alsoCheck: HTMLInputElement = checkbox()
    val hasFollow: Boolean = followingVacantRegular().isDefined
    val main: HTMLElement =
      div(
        kenshinCheck(onchange := (onKenshinChange _)),
        "健診",
        alsoWrapper(showHide := hasFollow)(
          alsoCheck(disabled := true),
          label("診察も")
        )
      )
    def tags: Set[String] =
      if kenshinCheck.checked then Set("健診") else Set.empty
    def isFollowingChecked: Boolean =
      alsoCheck.checked
    def onKenshinChange(event: Event): Unit =
      if kenshinCheck.checked then alsoCheck(disabled := false)
      else alsoCheck(disabled := true, checked := false)
