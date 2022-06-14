package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.fujiwara.kanjidate.DateParser

import java.time.LocalDate

object DateUtil:
  def getDateByDialog(title: String, handler: Option[LocalDate] => Unit): Unit =
    val input = Html.input
    val dlog = new ModalDialog3()
    dlog.title(title)
    dlog.body(input(width := "8rem"), placeholder := "R3.4.12")
    dlog.commands(
      button(
        "入力",
        onclick := (() => {
          DateParser.parse(input.value) match {
            case Some(d) => handler(Some(d))
            case _       => ShowMessage.showError("入力が不適切です。\n例：R3.4.12")
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

