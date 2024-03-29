package dev.fujiwara.domq.dateinput

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

import dev.fujiwara.domq.all.{*, given}

class EditableDate(
    initialValue: LocalDate,
    format: LocalDate => String = d => KanjiDate.dateToKanji(d),
    title: String = "日付の入力"
)(using DateInput.Suggest):
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

  def simulateChange(d: LocalDate): Unit =
    simulateChange(_ => d)

  private def updateUI(): Unit =
    ele(innerText := format(date))

  private def doClick(): Unit =
    val dlog = DateInputFormDialog(Some(date), allowNone = false)
    dlog.onEnter(_.foreach(simulateChange(_)))
    dlog.open()

class EditableDateOption(
    var initialValue: Option[LocalDate],
    format: LocalDate => String = d => KanjiDate.dateToKanji(d),
    formatNone: () => String = () => "（未入力）",
    title: String = "日付の入力"
)(using DateInput.Suggest):
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

  def simulateChange(dOption: Option[LocalDate]): Unit =
    simulateChange(_ => dOption)

  private def updateUI(): Unit =
    ele(innerText := dateOption.fold(formatNone())(format))

  private def doClick(): Unit =
    val dlog = DateInputFormDialog(dateOption)
    dlog.onEnter(dOpt => simulateChange(dOpt))
    dlog.open()

