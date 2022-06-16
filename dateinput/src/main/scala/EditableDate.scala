package dev.fujiwara.dateinput

import java.time.LocalDate

import dev.fujiwara.domq.all.{*, given}

case class EditableDate(var date: LocalDate, title: String)(using
    formatter: DateFormatConfig
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
    ele(innerText := formatter.format(date))

  def doEdit(): Unit =
    ManualInput.getDateByDialog(
      title,
      dateOpt =>
        dateOpt match {
          case None => ()
          case Some(d) =>
            date = d
            updateUI()
        },
      Some(date)
    )

case class EditableOptionalDate(
    var dateOption: Option[LocalDate],
    title: String,
    blankLabel: String,
    blankSuggest: Option[LocalDate]
)(using formatter: DateFormatConfig):
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
      dateOption.map(date => formatter.format(date)).getOrElse(blankString)
    ele(innerText := s)

  def doEdit(): Unit =
    ManualInput.getDateByDialog(
      title,
      dateOpt =>
        dateOpt match {
          case None => ()
          case Some(d) =>
            date = d
            updateUI()
        },
      blankSuggest
    )
