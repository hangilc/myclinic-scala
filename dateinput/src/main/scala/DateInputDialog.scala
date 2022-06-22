package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate

case class DateInputDialog[T](init: Option[LocalDate]):
  private val onEnterPublisher = new LocalEventPublisher[Option[LocalDate]]
  val input = Html.input
  val dlog = new ModalDialog3()
  dlog.content(cls := "domq-date-input-dialog")
  dlog.title(config.title)
  dlog.body(input(cls := "domq-date-input-dialog-input"))
  dlog.commands(
    button("入力", onclick := (doEnter _)),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open(): Unit = 
    dlog.open()

  private def doEnter(): Unit =
    ???