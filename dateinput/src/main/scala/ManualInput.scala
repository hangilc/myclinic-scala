package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.fujiwara.kanjidate.DateParser

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

object ManualInput:
  def getDateByDialog(
      onEnter: Option[LocalDate] => Unit,
  )(using config: ManualInputConfig): Unit =
    val input = Html.input
    val dlog = new ModalDialog3()
    dlog.title(config.title)
    dlog.body(
      input(cls := "width-8rem", value := formatValue),
      placeholder := "R3.4.12"
    )
    dlog.commands(
      button("入力", onclick := (doEnter _)),
      button("キャンセル", onclick := (() => dlog.close()))
    )
    dlog.open()

    def doEnter(): Unit =
      val src = input.value.trim
      (for
        parsed <-
          if src.isEmpty then Right(None)
          else DateParser.parse(src).toRight("入力が不適切です。").map(Option(_))
        checked <- config.check(parsed)
      yield checked).fold(msg => ShowMessage.showError(msg), onEnter)

    def formatValue: String =
      config.init match {
        case None => ""
        case Some(d) =>
          KanjiDate.dateToKanji(
            d,
            formatYear = (info => s"${info.gengouAlphaChar}${info.nen}."),
            formatMonth = (info => s"${info.month}."),
            formatDay = (info => s"${info.day}")
          )
      }

  case class ManualInputConfig(
    title: String,
    init: Option[LocalDate],
    check: Option[LocalDate] => Either[String, Option[LocalDate]] 
  )
