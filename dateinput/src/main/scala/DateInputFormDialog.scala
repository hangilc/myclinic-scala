package dev.fujiwara.dateinput

import java.time.LocalDate
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

class DateInputFormDialog(init: Option[LocalDate], title: String = "日付の入力"):
  val form = new DateInputForm(init)
  val dlog = new ModalDialog3()
  dlog.title(title)
  dlog.commands(
    button("入力",
    button("キャンセル", onclick := (() => dlog.close())))
  )

  def open(): Unit = 
    dlog.open()

