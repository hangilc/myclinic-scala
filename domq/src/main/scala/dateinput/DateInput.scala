package dev.fujiwara.domq.dateinput

import java.time.LocalDate

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import org.scalajs.dom.HTMLElement

case class DateOptionInput(
    private var initialValue: Option[LocalDate] = None,
    format: LocalDate => String = d => KanjiDate.dateToKanji(d),
    formatNone: () => String = () => "（未入力）",
    title: String = "日付の入力"
)(using InitNoneConverter):
  val dateEdit =
    EditableDateOption(initialValue, format, formatNone, title)
  val xCircleIcon = Icons.xCircle
  val icon = Icons.calendar
  val ele = div(
    cls := "domq-date-input date-option",
    dateEdit.ele,
    xCircleIcon(onclick := (doClear _)),
    icon(onclick := (doCalendar _))
  )
  updateUI(dateEdit.value)
  dateEdit.onChange(updateUI _)

  def value: Option[LocalDate] = dateEdit.value

  def init(newValue: Option[LocalDate]): Unit =
    dateEdit.init(newValue)

  def simulateChange(f: Option[LocalDate] => Option[LocalDate]): Unit =
    dateEdit.simulateChange(f)

  def onChange(handler: Option[LocalDate] => Unit): Unit =
    dateEdit.onChange(handler)

  private def updateUI(currentValue: Option[LocalDate]): Unit =
    currentValue match {
      case None => xCircleIcon(displayNone)
      case Some(_) => xCircleIcon(displayDefault)
    }

  private def doClear(): Unit =
    simulateChange(_ => None)

  private def doCalendar(): Unit =
    DateInputCommon.openCalendar(
      dateEdit.value,
      icon,
      d => simulateChange(_ => Some(d))
    )

case class DateInput(
    private var initialValue: LocalDate,
    format: LocalDate => String = d => KanjiDate.dateToKanji(d),
    title: String = "日付の入力"
)(using InitNoneConverter):
  val dateEdit = EditableDate(initialValue, format, title)
  val icon = Icons.calendar
  val ele = div(
    cls := "domq-date-input date-required",
    dateEdit.ele,
    icon(onclick := (doCalendar _))
  )

  def value: LocalDate = dateEdit.value
  def init(newValue: LocalDate): Unit = dateEdit.init(newValue)
  def simulateChange(f: LocalDate => LocalDate): Unit =
    dateEdit.simulateChange(f)

  def onChange(handler: LocalDate => Unit): Unit =
    dateEdit.onChange(handler)

  private def doCalendar(): Unit =
    DateInputCommon.openCalendar(
      Some(dateEdit.value),
      icon,
      d => simulateChange(_ => d)
    )

object DateInputCommon:
  def openCalendar(
      init: Option[LocalDate],
      icon: HTMLElement,
      onEnter: LocalDate => Unit
  )(using InitNoneConverter): Unit =
    val picker = DatePicker(init)
    picker.onDateSelected(onEnter)
    def locate(e: HTMLElement): Unit =
      Absolute.setLeftOf(e, Absolute.rightOf(icon) + 4)
      Absolute.setBottomOf(e, Absolute.topOf(icon) - 4)
      Absolute.ensureInViewOffsetting(e, 10)
    picker.open(locate)

