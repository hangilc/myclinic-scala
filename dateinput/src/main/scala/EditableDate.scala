package dev.fujiwara.dateinput

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

import dev.fujiwara.domq.all.{*, given}

class EditableDateBase(
  private var dateOption: Option[LocalDate],
  formatter: Option[LocalDate] => String,
  title: String
):
  private val onChangePublisher = new LocalEventPublisher[Option[LocalDate]]

  val ele = span(cls := "domq-editable-date", onclick := (doEdit _))
  updateUI()

  def optionValue: Option[LocalDate] = dateOption

  def set(newValue: Option[LocalDate]): Unit =
    dateOption = newValue
    updateUI()
    onChangePublisher.publish(dateOption)

  def onChange(handler: Option[LocalDate] => Unit): Unit =
    onChangePublisher.subscribe(handler)

  private def updateUI(): Unit =
    ele(innerText := formatter(dateOption))

  private def doEdit(): Unit =
    ManualInput.getDateOptionByDialog(set _, dateOption, title)

class EditableDate(init: LocalDate, formatter: LocalDate => String, title: String)
  extends EditableDateBase(Some(init), dateOption => {
    dateOption match {
      case None => ""
      case Some(d) => formatter(d)
    }
  }, title):
  def onChange(handler: LocalDate => Unit): Unit =
    super.onChange(dateOption => dateOption match {
      case Some(d) => handler(d)
      case None => ()
    })

  def set(value: LocalDate): Unit =
    super.set(Some(value))

case class EditableDate(
    var date: LocalDate,
    formatter: LocalDate => String = EditableDate.defaultFormatter,
    title: String = "日付の入力"
):
  val ele = span(cls := "cursor-pointer", onclick := (doEdit _))
  updateUI()

  def set(newValue: LocalDate): Unit =
    date = newValue
    updateUI()

  def incDays(days: Int): Unit =
    date = date.plusDays(days)
    updateUI()

  def incMonths(months: Int): Unit =
    date = date.plusMonths(months)
    updateUI()

  def incYears(years: Int): Unit =
    date = date.plusYears(years)
    updateUI()

  def updateUI(): Unit =
    ele(innerText := formatter(date))

  def doEdit(): Unit =
    ManualInput.getDateByDialog(set _, init = Some(date), title = title)

object EditableDate:
  val defaultFormatter: LocalDate => String =
    d => KanjiDate.dateToKanji(d)

case class EditableOptionalDate(
    var dateOption: Option[LocalDate],
    formatter: LocalDate => String = EditableDate.defaultFormatter,
    nullFormatter: () => String = () => "",
    title: String = "日付の入力"
):
  private val onChangePublisher = new LocalEventPublisher[Option[LocalDate]]
  val ele = span(cls := "cursor-pointer", onclick := (doEdit _))
  updateUI()

  def set(newValue: Option[LocalDate]): Unit =
    dateOption = newValue
    updateUI()

  def onChange(handler: Option[LocalDate] => Unit): Unit =
    onChangePublisher.subscribe(handler)

  def changeDate(f: LocalDate => LocalDate): Unit =
    dateOption.foreach(date =>
      dateOption = Some(f(date))
      updateUI()
      onChangePublisher.publish(Some(date))  
    )

  def incDays(days: Int): Unit =
    changeDate(_.plusDays(days))

  def incMonths(months: Int): Unit =
    changeDate(_.plusMonths(months))

  def incYears(years: Int): Unit =
    changeDate(_.plusYears(years))

  private def format(opt: Option[LocalDate]): String =
    opt match {
      case None => nullFormatter()
      case Some(d) => formatter(d)
    }

  def updateUI(): Unit =
    ele(innerText := format(dateOption))

  def doEdit(): Unit =
    ManualInput.getDateOptionByDialog(dateOption => {
      set(dateOption)
      onChangePublisher.publish(dateOption)
    }, dateOption, title)
