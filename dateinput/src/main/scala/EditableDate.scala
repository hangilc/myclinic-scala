package dev.fujiwara.dateinput

import java.time.LocalDate

import dev.fujiwara.domq.all.{*, given}

case class EditableDate(var date: LocalDate, title: String)(using formatter: DateFormatConfig):
  val ele = span(cls := "cursor-pointer", onclick := (doEdit _))
  updateUI()

  def updateUI(): Unit =
    ele(innerText := formatter.format(date))

  def doEdit(): Unit =
    ManualInput.getDateByDialog(title, dateOpt => dateOpt match {
      case None => ()
      case Some(d) =>
        date = d
        updateUI()
    }, Some(date))
