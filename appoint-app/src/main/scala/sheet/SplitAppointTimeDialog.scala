package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.{Modal, Form, ErrorBox, ShowMessage}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.web.appoint.Misc
import java.time.LocalTime
import dev.myclinic.scala.util.DateTimeOrdering
import scala.math.Ordered.orderingToOrdered

class SplitAppointTimeDialog(appointTime: AppointTime):
  def open(): Unit =
    makeDialog().open()

  def makeDialog(): Modal =
    val errBox = ErrorBox()
    val splitTimeInput = inputText()
    val execButton = Modal.execute
    val cancelButton = Modal.cancel
    val dialog = Modal(
      "予約枠の分割",
      div(
        errBox.ele,
        div(Misc.formatAppointDate(appointTime.date)),
        div(
          "%s - %s".format(
            Misc.formatAppointTime(appointTime.fromTime),
            Misc.formatAppointTime(appointTime.untilTime)
          )
        ),
        div(
          "分割時刻：",
          splitTimeInput(placeholder := "HH:MM")
        )
      ),
      div(execButton, cancelButton)
    )
    execButton(onclick := (() => { 
      onExec(splitTimeInput.value, errBox)
      dialog.close() 
    }))
    cancelButton(onclick := (() => dialog.close()))
    dialog

  def onExec(splitTime: String, errBox: ErrorBox): Unit =
    val v = SplitValidator.validate(splitTime, appointTime)
    SplitValidator.toEither(v) match {
      case Right(at) => Api.splitAppointTime(appointTime.appointTimeId, at)
      case Left(msg) => errBox.show(msg)
    }

  object SplitValidator:
    import cats.data.ValidatedNec
    import cats.data.Validated.{validNec, invalidNec, condNec}
    import dev.myclinic.scala.validator.Validators

    sealed trait SplitError(val label: String)
    object EmptyInputError extends SplitError("分割時刻が入力されていません。")
    object InvalidFormatError extends SplitError("分割時刻の入力フォーマットが不適切です。")
    object InvalidTimeError extends SplitError("分割時刻の値が不適切です。")
    object InvalidSplitPointError extends SplitError("分割時刻が予約枠外です。")

    type Result = ValidatedNec[SplitError, LocalTime]

    def validateSplitTime(input: String, appointTime: AppointTime): Result =
      Validators
        .nonEmpty(input, EmptyInputError)
        .andThen(
          Validators.regexMatch(_, raw"\d{2}:\d{2}".r, InvalidFormatError)
        )
        .andThen(input => {
          try validNec(LocalTime.parse(input))
          catch {
            case _: Throwable => invalidNec(InvalidFormatError)
          }
        })
        .andThen(t =>
          condNec(
            appointTime.fromTime <= t && t <= appointTime.untilTime,
            t,
            InvalidSplitPointError
          )
        )

    def validate(splitTimeInput: String, appointTime: AppointTime): Result =
      validateSplitTime(splitTimeInput, appointTime)

    def toEither(result: Result): Either[String, LocalTime] =
      Validators.toEither(result, _.label)

    
