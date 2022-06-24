package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Absolute
import scala.language.implicitConversions
import java.time.LocalDate
import org.scalajs.dom.MouseEvent

case class MonthDisp(var month: Int):
  private val monhtChangePublisher = new LocalEventPublisher[Int]
  val monthSpan = span
  val ele = div(cls := "domq-user-select-none",
    cls := "domq-display-inline-block domq-date-picker-month-disp",
    Icons.chevronLeft(cls := "domq-icon-chevron-left", onclick := (() => simulateChange(_ - 1))),
    monthSpan(onclick := (doMonthClick _)),
    Icons.chevronRight(cls := "domq-icon-chevron-right", onclick := (() => simulateChange(_ + 1)))
  )
  updateUI()

  def onChangeMonth(handler: Int => Unit): Unit =
    monhtChangePublisher.subscribe(handler)

  def set(newMonth: Int): Unit =
    month = newMonth
    updateUI()

  def simulateChange(f: Int => Int): Unit =
    set(f(month))
    monhtChangePublisher.publish(month)

  def updateUI(): Unit =
    monthSpan(innerText := f"${month}%02d月")

  def doMonthClick(event: MouseEvent): Unit =
    val (x, y) = Absolute.clickPos(event)
    val monthList = MonthList()
    monthList.ele(cls := "domq-background-white")
    Absolute.position(monthList.ele)
    val close: () => Unit = Absolute.openWithScreen(monthList.ele, e => {
      Absolute.setLeftOf(e, x + 8)
      Absolute.setBottomOf(e, y + 20)
      Absolute.ensureInViewOffsetting(e, 10)
    })
    monthList.selection.addSelectEventHandler(m => {
      close()
      simulateChange(_ => m)
    })

