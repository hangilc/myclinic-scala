package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.fujiwara.kanjidate.DateParser

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate

object ManualInput:
  def getDateByDialog(
      title: String,
      handler: Option[LocalDate] => Unit,
      init: Option[LocalDate] = None,
      acceptNone: Boolean = false
  ): Unit =
    val input = Html.input
    val dlog = new ModalDialog3()
    dlog.title(title)
    dlog.body(input(width := "8rem", value := formatValue), placeholder := "R3.4.12")
    dlog.commands(
      button(
        "入力",
        onclick := (() => {
          DateParser.parse(input.value) match {
            case Some(d) =>
              dlog.close()
              handler(Some(d))
            case _ => ShowMessage.showError("入力が不適切です。\n例：R3.4.12")
          }
        })
      ),
      button(
        "キャンセル",
        onclick := (() => {
          dlog.close()
          handler(None)
        })
      )
    )
    dlog.open()

    def formatValue: String =
      init match {
        case None => ""
        case Some(d) => KanjiDate.dateToKanji(d, 
          formatYear = (info => s"${info.gengouAlphaChar}${info.nen}."),
          formatMonth = (info => s"${info.month}."),
          formatDay = (info => s"${info.day}")
        )
      }
      
