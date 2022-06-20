package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{Absolute}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import scala.collection.mutable.ListBuffer
import dev.fujiwara.dateinput.datepicker.*
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import dev.fujiwara.kanjidate.KanjiDate

case class DatePicker(init: Option[LocalDate]):
  private val dateSelectedPublisher = new LocalEventPublisher[LocalDate]
  private val (initGengou: Gengou, initNen: Int, initMonth: Int) =
    init.orElse(Some(LocalDate.now()))
    .map(d => 
      val (g, n) = Gengou.dateToGengou(d).get
      (g, n, d.getMonthValue)
    ).get
  private val initDay: Option[Int] = init.map(_.getDayOfMonth)
  val yearDisp = YearDisp(initGengou, initNen)
  val monthDisp = MonthDisp(initMonth)
  val datesTab = div
  val ele = div(cls := "domq-date-picker domq-user-select-none",
    div(cls := "year-nen",
      yearDisp.ele, monthDisp.ele, Icons.cog(cls := "domq-icon-cog")
    ),
    datesTab(cls := "domq-date-picker-dates-tab"),
    css(_.position = "absolute")
  )
  yearDisp.onChangeYear(doChangeYear _)
  monthDisp.onChangeMonth(doChangeMonth _)
  var close: () => Unit = () => ()

  def onDateSelected(handler: LocalDate => Unit): Unit =
    dateSelectedPublisher.subscribe(handler)

  def open(locator: HTMLElement => Unit): Unit =
    var cur: LocalDate = init.getOrElse(LocalDate.now()) 
    stuffDates(cur.getYear, cur.getMonthValue)
    close = Absolute.openWithScreen(ele, locator)

  private def doChangeYear(newYear: Int): Unit =
    val tmpDay = initDay.getOrElse(1)
    val d = tmpDay.min(KanjiDate.lastDayOfMonth(newYear, monthDisp.month).getDayOfMonth)
    val (g, n) = Gengou.dateToGengou(LocalDate.of(newYear, monthDisp.month, d)).get
    yearDisp.set(g, n)
    stuffDates(yearDisp.year, monthDisp.month)

  private def doChangeMonth(newMonth: Int): Unit =
    val diff = newMonth - monthDisp.month
    val (targetYear, targetMonth) = 
      val t = LocalDate.of(yearDisp.year, monthDisp.month, 1).plusMonths(diff)
      (t.getYear, t.getMonthValue)
    val tmpDay = initDay.getOrElse(1)
    val d = tmpDay.min(KanjiDate.lastDayOfMonth(targetYear, targetMonth).getDayOfMonth)
    val (g, n) = Gengou.dateToGengou(LocalDate.of(targetYear, targetMonth, d)).get
    yearDisp.set(g, n)
    monthDisp.set(targetMonth)
    stuffDates(yearDisp.year, monthDisp.month)

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
    val tmpDay = initDay.map(_.min(KanjiDate.lastDayOfMonth(year, month).getDayOfMonth))
    while d.isBefore(end) do
      buf.addOne(d)
      d = d.plusDays(1)
    buf.toList.map(d => {
      val e = div(d.getDayOfMonth.toString)(
        cls := "domq-date-picker-date-box",
        onclick := (() => onDayClick(d)),
        attr("data-date") := d.getDayOfMonth.toString
      )
      if Some(d) == init then e(cls := "init-date")
      else if Some(d.getDayOfMonth) == tmpDay then e(cls := "init-date-equiv")
      e
    })

  private def onDayClick(d: LocalDate): Unit =
    close()
    dateSelectedPublisher.publish(d)

