package dev.fujiwara.dateinput

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, FloatingElement, Geometry}
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.language.implicitConversions
import cats.*
import cats.syntax.*
import cats.implicits.*
import cats.data.Validated.{validNec, invalidNec, condNec}
import cats.data.ValidatedNec
import java.time.LocalDate
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.raw.Event

class DateInput(gengouList: List[Gengou] = Gengou.list,
    onEnter: LocalDate => Unit = _ => (),
    onChange: LocalDate => Unit = _ => ()):
  val eInput: HTMLInputElement = inputText(placeholder := "平成３０年１２月２３日")
  val eCalendar = Icons.calendar
  val ele: HTMLElement = div(cls := "domq-date-input-wrapper")(
    form(eInput(cls := "domq-date-input"), onsubmit := (onSubmit _)),
    eCalendar(
      Icons.defaultStyle,
      onclick := (openPicker _)
    )
  )

  private def onSubmit(): Unit = {
    validate() match {
      case Valid(d) => onEnter(d)
      case Invalid(_) => ()
    }
  }

  def setDate(date: LocalDate): Unit =
    eInput(value := KanjiDate.dateToKanji(date))

  def locatePicker(f: FloatingElement): Unit =
    val r = Geometry.getRect(eCalendar)
    val p = r.leftTop
    f.leftTop = p

  def openPicker(event: MouseEvent): Unit =
    val a = validate().asEither match {
      case Right(d) => d
      case Left(_) => LocalDate.now()
    }
    new DatePicker(d => {
      setDate(d)
      onChange(d)
    }, (locatePicker _)).open(event, a.getYear, a.getMonthValue)

  def validate(): DateInputValidator.Result[LocalDate] =
    DateInputValidator.validateDateInput(eInput.value)

  def validateOption(): DateInputValidator.Result[Option[LocalDate]] =
    if eInput.value.isEmpty then validNec(None)
    else DateInputValidator.validateDateInput(eInput.value).map(Some(_))

