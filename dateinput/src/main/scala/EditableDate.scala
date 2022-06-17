package dev.fujiwara.dateinput

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

import dev.fujiwara.domq.all.{*, given}

case class EditableDate(
    var date: LocalDate,
    formatter: LocalDate => String = (d => KanjiDate.dateToKanji(d)),
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

case class EditableOptionalDate(
    var dateOption: Option[LocalDate],
    formatter: Option[LocalDate] => String,
    blankSuggest: Option[LocalDate]
)(using formatter: DateFormatConfig, manualInputConfig: ManualInputConfig):
  val ele = span(cls := "cursor-pointer", onclick := (doEdit _))
  updateUI()

  def set(newValue: Option[LocalDate]): Unit =
    dateOption = newValue
    updateUI()

  def incDays(days: Int): Unit =
    dateOption.foreach(date =>
      dateOption = Some(date.plusDays(days))
      updateUI()
    )

  def incMonths(months: Int): Unit =
    dateOption.foreach(date =>
      dateOption = Some(date.plusMonths(months))
      updateUI()
    )

  def incYears(years: Int): Unit =
    dateOption.foreach(date =>
      dateOption = Some(date.plusYears(years))
      updateUI()
    )

  def updateUI(): Unit =
    val s =
      dateOption.map(date => formatter.format(date)).getOrElse(blankLabel)
    ele(innerText := s)

  def doEdit(): Unit =
    val mconfig = manualInputConfig.copy(
      init =
    )
    ManualInput.getDateByDialog(
      dateOpt =>
        dateOpt match {
          case None => ()
          case Some(d) =>
            date = d
            updateUI()
        },
    )
