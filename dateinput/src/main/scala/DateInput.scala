package dev.fujiwara.dateinput

import java.time.LocalDate

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import org.scalajs.dom.HTMLElement

case class DateInput(
    private var init: Option[LocalDate] = None,
    formatter: LocalDate => String = DateInput.defaultFormatter,
    nullFormatter: () => String = () => "",
    title: String = "日付の入力"
):
  private val onChangePublisher = new LocalEventPublisher[LocalDate]
  val dateEdit =
    EditableOptionalDate(init, formatter = formatter, title = title)
  val icon = Icons.calendar
  val ele = div(
    cls := "domq-date-input",
    dateEdit.ele,
    icon(cls := "domq-calendar-icon", onclick := (doCalendar _))
  )

  def value: Option[LocalDate] = dateEdit.dateOption
  def set(value: Option[LocalDate]): Unit = dateEdit.set(value)
  def set(value: LocalDate): Unit = set(Some(value))
  def unset(): Unit = set(None)

  def onChange(handler: LocalDate => Unit): Unit =
    onChangePublisher.subscribe(handler)

  def changeDate(f: LocalDate => LocalDate): Unit =
    dateEdit.changeDate(f)

  private def doCalendar(): Unit =
    val picker = DatePicker(dateEdit.dateOption)
    picker.onDateSelected(d => 
      dateEdit.set(Some(d))
      onChangePublisher.publish(d)
    )
    // Absolute.enableDrag(picker.ele, picker.ele)
    def locate(e: HTMLElement): Unit =
      Absolute.setLeftOf(e, Absolute.rightOf(icon) + 4)
      Absolute.setBottomOf(e, Absolute.topOf(icon) - 4)
      Absolute.ensureInViewOffsetting(e, 10)
    picker.open(locate)

object DateInput:
  val defaultFormatter: LocalDate => String =
    d => KanjiDate.dateToKanji(d)
