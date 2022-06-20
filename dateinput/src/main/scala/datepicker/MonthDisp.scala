package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class MonthDisp(var month: Int):
  val monthSpan = span
  val ele = div(cls := "domq-display-inline-block",
    monthSpan
  )
  updateUI()

  def updateUI(): Unit =
    monthSpan(innerText := s"${month}月")


