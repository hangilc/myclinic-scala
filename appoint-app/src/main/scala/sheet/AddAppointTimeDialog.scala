package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.{Modal, Form, ErrorBox}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.AppointTimeValidator
import dev.myclinic.scala.web.appbase.AppointTimeValidator.{given}
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future

class AddAppointTimeDialog(date: LocalDate):
  val eFromTime = inputText()
  val eUntilTime = inputText()
  val eKind = inputText()
  val eCapacity = inputText()
  val eError = ErrorBox()
  val dlog: Modal = Modal(
    "予約枠追加",
    div(
      Form.rows(
        span("開示時間") -> eFromTime(placeholder := "HH:MM"),
        span("終了時間") -> eUntilTime(placeholder := "HH:MM"),
        span("種類") -> eKind(value := "regular"),
        span("人数") -> eCapacity(value := "1"),
      ),
      eError.ele
    ),
    div(
      button("入力", onclick := (doEnter _)),
      button("キャンセル", onclick := (() => dlog.close()))
    )
  )
  def open(): Unit = dlog.open()

  def initFocus(): Unit = eFromTime.focus()

  def doEnter(): Unit = 
    val f = 
      AppointTimeValidator.validateForEnter(
        date,
        AppointTimeValidator.validateFromTimeInput(eFromTime.value),
        AppointTimeValidator.validateUntilTimeInput(eUntilTime.value),
        AppointTimeValidator.validateKindInput(eKind.value),
        AppointTimeValidator.validateCapacityInput(eCapacity.value)
      ).asEither match {
        case Right(appointTime) => Api.addAppointTime(appointTime)
        case Left(msg) => Future.failed(new RuntimeException(msg))
      }
    f.onComplete {
      case Success(_) => dlog.close()
      case Failure(ex) => eError.show(ex.getMessage)
    }