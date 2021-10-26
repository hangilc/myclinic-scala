package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.ContextMenu
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.Form
import dev.fujiwara.domq.ShowMessage
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dev.myclinic.scala.validator.AppointTimeValidator
import dev.myclinic.scala.web.appoint.Misc
import cats.data.ValidatedNec
import cats.data.Validated.{validNec, invalidNec, condNec, Valid, Invalid}
import dev.myclinic.scala.validator.Validators
import java.time.LocalTime
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

class AdminAppointTimeBox(appointTime: AppointTime)
    extends AppointTimeBox(appointTime):
  ele.addEventListener(
    "contextmenu",
    (event: MouseEvent) => {
      event.preventDefault
      ContextMenu(
        "Convert" -> doConvert,
        "Combine" -> doCombine,
        "Split" -> doSplit
      ).show(event)
    }
  )

  def doConvert(): Unit =
    ConvertAppointTimeDialog(appointTime).open()

  def doCombine(): Unit =
    for
      appointTimes <- Api.listAppointTimesForDate(appointTime.date)
    yield {
      CombineAppointTimesDialog(appointTime, appointTimes).open()
    }

    // val nFollows = 1
    // def listFollows(appointTimes: List[AppointTime]): List[AppointTime] =
    //   appointTimes
    //     .dropWhile(_.appointTimeId != appointTime.appointTimeId)
    //     .sliding(2)
    //     .takeWhile({
    //       case a :: b :: _ => a.isAdjacentTo(b)
    //       case _           => false
    //     })
    //     .map(_(1))
    //     .toList
    // for
    //   appointTimes <- Api.listAppointTimesForDate(appointTime.date)
    // yield {
    //   val follows = listFollows(appointTimes).take(nFollows)
    //   if follows.isEmpty then
    //     ShowMessage.showMessage("結合する予約枠がありません。")
    //     Future.unit
    //   else
    //     val lines = List(
    //       "以下のように予約枠を結合します。",
    //       Misc.formatAppointDate(appointTime.date),
    //       Misc.formatAppointTime(appointTime.fromTime) + " - ",
    //       Misc.formatAppointTime(follows.last.untilTime)
    //     ).mkString("\n")
        // Modal("予約枠の結合", (close, body, commands) => {
        //   body.innerText = lines
        //   commands(
        //     Modal.ok()(onclick := { () => 
        //       val ids = (appointTime :: follows).map(_.appointTimeId)
        //       Api.combineAppointTimes(ids)
        //       close()
        //       }),
        //     Modal.cancel()(onclick := (() => close()))
        //   )
        // }).open()

  def doSplit(): Unit = 
    ()
    // val errorBox = div()
    // val splitTimeInput = inputText()
    // def setupBody(body: HTMLElement): Unit = 
    //   body(
    //     errorBox(color := "red"),
    //     div(Misc.formatAppointDate(appointTime.date)),
    //     div("%s - %s".format(
    //       Misc.formatAppointTime(appointTime.fromTime),
    //       Misc.formatAppointTime(appointTime.untilTime)
    //     )),
    //     div(
    //       "分割時刻：",
    //       splitTimeInput(placeholder := "HH:MM")
    //     )
    //   )
    // def setupCommands(close: Modal.CloseFunction, wrapper: HTMLElement): Unit =
    //   wrapper(
    //     button("実行", onclick := (() => onExecute(close))),
    //     button("キャンセル", onclick := close)
    //   )
    // def onExecute(close: Modal.CloseFunction): Unit =
    //   SplitValidator.validate(splitTimeInput.value, appointTime) match {
    //     case Valid(at) => {
    //       Api.splitAppointTime(appointTime.appointTimeId, at)
    //       close()
    //     }
    //     case Invalid(e) => { 
    //       errorBox.innerText = Validators.errorMessage(e, _.label)
    //     }
    //   }
    // Modal("予約枠の分割", (close, body, commands) => {
    //   setupBody(body)
    //   setupCommands(close, commands)
    // }).open()

private object SplitValidator:
  sealed trait SplitError(val label: String)
  object EmptyInputError extends SplitError("分割時刻が入力されていません。")
  object InvalidFormatError extends SplitError("分割時刻の入力フォーマットが不適切です。")
  object InvalidTimeError extends SplitError("分割時刻の値が不適切です。")
  object InvalidSplitPointError extends SplitError("分割時刻が予約枠外です。")

  type Result = ValidatedNec[SplitError, LocalTime]

  def validateSplitTime(input: String, appointTime: AppointTime): Result =
    Validators.nonEmpty(input, EmptyInputError)
    .andThen(Validators.regexMatch(_, raw"\d{2}:\d{2}".r, InvalidFormatError))
    .andThen(input => {
      try
        validNec(LocalTime.parse(input))
      catch {
        case _: Throwable => invalidNec(InvalidFormatError)
      }
    })
    .andThen(t => condNec(
      appointTime.fromTime <= t && t <= appointTime.untilTime,
      t,
      InvalidSplitPointError
    ))

  def validate(splitTimeInput: String, appointTime: AppointTime): Result =
    validateSplitTime(splitTimeInput, appointTime)



