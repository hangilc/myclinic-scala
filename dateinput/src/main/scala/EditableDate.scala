package dev.fujiwara.dateinput

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

import dev.fujiwara.domq.all.{*, given}

class EditableDate(
    initialValue: LocalDate,
    format: LocalDate => String = d => KanjiDate.dateToKanji(d),
    title: String = "日付の入力"
):
  private val onChangePublisher = new LocalEventPublisher[LocalDate]
  private var date: LocalDate = initialValue
  val ele = span(cls := "domq-editable-date domq-cursor-pointer", onclick := (doClick _))
  updateUI()

  def value: LocalDate = date

  def onChange(handler: LocalDate => Unit): Unit =
    onChangePublisher.subscribe(handler)

  def init(newValue: LocalDate): Unit =
    date = newValue
    updateUI()

  def simulateChange(f: LocalDate => LocalDate): Unit =
    date = f(date)
    updateUI()
    onChangePublisher.publish(date)

  private def updateUI(): Unit =
    ele(innerText := format(date))

  private def doClick(): Unit =
    val dlog = DateInputDialog(date)
    dlog.onEnter(d => simulateChange(_ => d))

class EditableDateOption(
    var initialValue: Option[LocalDate],
    format: LocalDate => String = d => KanjiDate.dateToKanji(d),
    formatNone: () => String = () => "",
    title: String = "日付の入力"
):
  private val onChangePublisher = new LocalEventPublisher[Option[LocalDate]]
  private var dateOption: Option[LocalDate] = initialValue
  val ele = span(cls := "domq-editable-date domq-cursor-pointer", onclick := (doClick _))
  updateUI()

  def value: Option[LocalDate] = dateOption

  def onChange(handler: Option[LocalDate] => Unit): Unit =
    onChangePublisher.subscribe(handler)

  def init(newValue: Option[LocalDate]): Unit =
    dateOption = newValue
    updateUI()

  def simulateChange(f: Option[LocalDate] => Option[LocalDate]): Unit =
    dateOption = f(dateOption)
    updateUI()
    onChangePublisher.publish(dateOption)

  private def updateUI(): Unit =
    ele(innerText := dateOption.fold(formatNone())(format))

  private def doClick(): Unit =
    val dlog = DateOptionInputDialog(dateOption)
    dlog.onEnter(d => simulateChange(_ => d))

