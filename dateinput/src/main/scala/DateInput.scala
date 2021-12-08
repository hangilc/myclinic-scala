package dev.fujiwara.dateinput

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
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

class DateInput(gengouList: List[Gengou] = Gengou.list):
  val eInput: HTMLInputElement = inputText(placeholder := "平成３０年１２月２３日")
  val ele: HTMLElement = div(cls := "domq-date-input-wrapper")(
    eInput(cls := "domq-date-input"),
    Icons.calendar(color = "gray")(
      Icons.defaultStyle,
      onclick := (openPicker _)
    )
  )
  def setDate(date: LocalDate): Unit =
    println(("date", date, KanjiDate.dateToKanji(date)))
    eInput(value := KanjiDate.dateToKanji(date))

  def openPicker(event: MouseEvent): Unit =
    val a = validate().asEither match {
      case Right(d) => d
      case Left(_) => LocalDate.now()
    }
    new DatePicker(setDate).open(event, a.getYear, a.getMonthValue)

  def validate(): DateInputValidator.Result[LocalDate] =
    DateInputValidator.validateDateInput(eInput.value)

