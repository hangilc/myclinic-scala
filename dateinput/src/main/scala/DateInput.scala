package dev.fujiwara.dateinput

import java.time.LocalDate

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate

case class DateInput(
    private var init: Option[LocalDate] = None,
    formatter: LocalDate => String = DateInput.defaultFormatter,
    nullFormatter: () => String = () => "",
    title: String = "日付の入力"
):
  val dateEdit =
    EditableOptionalDate(init, formatter = formatter, title = title)
  val ele = div(
    cls := "domq-date-input",
    dateEdit.ele,
    Icons.calendar(cls := "domq-calendar-icon", onclick := (doCalendar _))
  )

  def value: Option[LocalDate] = dateEdit.dateOption
  def set(value: Option[LocalDate]) = dateEdit.set(value)

  private def doCalendar(): Unit =
    val picker = DatePicker()
    picker.open()

object DateInput:
  val defaultFormatter: LocalDate => String =
    d => KanjiDate.dateToKanji(d)
