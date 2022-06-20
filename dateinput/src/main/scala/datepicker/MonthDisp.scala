package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import java.time.LocalDate

case class MonthDisp(var month: Int):
  val monthSpan = span
  val ele = div(
    cls := "domq-display-inline-block domq-date-picker-month-disp",
    Icons.chevronLeft(cls := "domq-icon-chevron-left"),
    monthSpan,
    Icons.chevronRight(cls := "domq-icon-chevron-right")
  )
  updateUI()

  def updateUI(): Unit =
    monthSpan(innerText := s"${month}月")

