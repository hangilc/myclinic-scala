package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import java.time.LocalDate

case class MonthDisp(var month: Int):
  private val monhtChangePublisher = new LocalEventPublisher[Int]
  val monthSpan = span
  val ele = div(
    cls := "domq-display-inline-block domq-date-picker-month-disp",
    Icons.chevronLeft(cls := "domq-icon-chevron-left", onclick := (() => doChangeMonth(-1))),
    monthSpan,
    Icons.chevronRight(cls := "domq-icon-chevron-right", onclick := (() => doChangeMonth(1)))
  )
  updateUI()

  def set(newMonth: Int): Unit =
    month = newMonth
    updateUI()

  def onChangeMonth(handler: Int => Unit): Unit =
    monhtChangePublisher.subscribe(handler)

  def doChangeMonth(n: Int): Unit =
    monhtChangePublisher.publish(month + n)

  def updateUI(): Unit =
    monthSpan(innerText := f"${month}%02d月")

