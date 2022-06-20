package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{Absolute}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import scala.collection.mutable.ListBuffer
import dev.fujiwara.dateinput.datepicker.*
import dev.fujiwara.kanjidate.KanjiDate.Gengou

case class DatePicker():
  val yearDisp = YearDisp(Gengou.Reiwa, 4)
  val monthDisp = MonthDisp(6)
  val datesTab = div
  val ele = div(
    div(
      yearDisp.ele, monthDisp.ele
    ),
    datesTab(cls := "domq-date-picker-dates-tab"),
    css(_.position = "absolute")
  )

  def open(locator: HTMLElement => Unit): Unit =
    val init = LocalDate.now()
    stuffDates(init.getYear, init.getMonthValue)
    Absolute.openWithScreen(ele, locator)

  private def stuffDates(year: Int, month: Int): Unit =
    datesTab(clear)
    val d1 = LocalDate.of(year, month, 1)
    val pad = d1.getDayOfWeek.getValue % 7
    for _ <- 0 until pad do datesTab(div())
    datesTab(children := makeDates(year, month))

  private def makeDates(year: Int, month: Int): List[HTMLElement] =
    val start = LocalDate.of(year, month, 1)
    val end = start.plusMonths(1)
    var d = start
    val buf = ListBuffer[LocalDate]()
    while d.isBefore(end) do
      buf.addOne(d)
      d = d.plusDays(1)
    buf.toList.map(d => {
      div(d.getDayOfMonth.toString)(
        cls := "domq-date-picker-date-box",
        onclick := (() => onDayClick(d)),
        attr("data-date") := d.getDayOfMonth.toString
      )
    })

  private def onDayClick(d: LocalDate): Unit =
    ()

