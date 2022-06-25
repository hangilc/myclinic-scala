package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{Absolute}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import scala.collection.mutable.ListBuffer
import dev.fujiwara.dateinput.datepicker.*
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import dev.fujiwara.kanjidate.KanjiDate.Era
import dev.fujiwara.kanjidate.KanjiDate
import java.time.DayOfWeek
import dev.fujiwara.kanjidate.DateUtil
import dev.fujiwara.kanjidate.DateUtil.given

case class DatePicker(init: Option[LocalDate])(
  using initNoneConverter: InitNoneConverter
):
  private val dateSelectedPublisher = new LocalEventPublisher[LocalDate]
  private val convertedInit: Option[LocalDate] = init.orElse(initNoneConverter.convert)
  private val (initYear, initGengou: Era, initNen: Int, initMonth: Int) =
    convertedInit.orElse(Some(LocalDate.now()))
    .map(d => 
      val (e, n) = Gengou.dateToEra(d)
      (d.getYear, e, n, d.getMonthValue)
    ).get
  private val initDay: Option[Int] = convertedInit.map(_.getDayOfMonth)
  val yearDisp = YearDisp(initYear)
  val monthDisp = MonthDisp(initMonth)
  val hand = Icons.hand
  val cog = Icons.cog
  val datesTab = div
  val ele = div(cls := "domq-date-picker domq-user-select-none",
    div(cls := "year-nen",
      yearDisp.ele, monthDisp.ele, hand, cog(displayNone)
    ),
    datesTab(cls := "domq-date-picker-dates-tab"),
    css(_.position = "absolute")
  )
  yearDisp.onChangeYear(doChangeYear _)
  monthDisp.onChangeMonth(doChangeMonth _)
  Absolute.enableDrag(ele, hand)
  var close: () => Unit = () => ()

  def onDateSelected(handler: LocalDate => Unit): Unit =
    dateSelectedPublisher.subscribe(handler)

  def open(locator: HTMLElement => Unit): Unit =
    var cur: LocalDate = init.getOrElse(LocalDate.now()) 
    stuffDates(cur.getYear, cur.getMonthValue)
    close = Absolute.openWithScreen(ele, locator)

  private def doChangeYear(newYear: Int): Unit =
    yearDisp.set(newYear)
    stuffDates(yearDisp.year, monthDisp.month)

  private def doChangeMonth(newMonth: Int): Unit =
    val diff = newMonth - monthDisp.month
    val (targetYear, targetMonth) = 
      val t = LocalDate.of(yearDisp.year, monthDisp.month, 1).plusMonths(diff)
      (t.getYear, t.getMonthValue)
    val tmpDay = initDay.getOrElse(1)
    val d = tmpDay.min(KanjiDate.lastDayOfMonth(targetYear, targetMonth).getDayOfMonth)
    yearDisp.set(targetYear)
    monthDisp.set(targetMonth)
    stuffDates(yearDisp.year, monthDisp.month)

  private enum CalDate(val date: LocalDate):
    case PreDate(date) extends CalDate(date)
    case MonthDate(date) extends CalDate(date)
    case PostDate(date) extends CalDate(date)

  private def listCalendarDates(year: Int, month: Int): List[CalDate] =
    import CalDate.*
    val start: LocalDate = LocalDate.of(year, month, 1)
    val end: LocalDate = start.plusMonths(1).minusDays(1)
    val preMonthStart: LocalDate = 
      if start.getDayOfWeek == DayOfWeek.SUNDAY then start.minusDays(7)
      else DateUtil.startDayOfWeek(start)
    val postMonthEnd: LocalDate = 
      if end.getDayOfWeek == DayOfWeek.SATURDAY then end.plusDays(7)
      else end.plusDays(6 - (end.getDayOfWeek.getValue % 7))
    DateUtil.enumDates(preMonthStart, start.minusDays(1)).map(PreDate(_))
      ++ DateUtil.enumDates(start, end).map(MonthDate(_))
      ++ DateUtil.enumDates(end.plusDays(1), postMonthEnd).map(PostDate(_))

  private def stuffDates(year: Int, month: Int): Unit =
    datesTab(clear, children := makeDates(listCalendarDates(year, month)))

  private def makeDates(calDates: List[CalDate]): List[HTMLElement] =
    val tmpDay = initDay.map(_.min(KanjiDate.lastDayOfMonth(year, month).getDayOfMonth))
    dates.map(d => {
      val e = div(d.getDayOfMonth.toString)(
        cls := "domq-date-picker-date-box",
        onclick := (() => onDayClick(d)),
        attr("data-date") := d.getDayOfMonth.toString
      )
      if Some(d) == init then e(cls := "init-date")
      else if Some(d.getDayOfMonth) == tmpDay then e(cls := "init-date-equiv")
      d.getDayOfWeek match 
        case DayOfWeek.SUNDAY => e(cls := "sunday")
        case DayOfWeek.SATURDAY => e(cls := "saturday")
        case _ => ()
      if d < start then e(cls := "pre-month")
      else if d > end then e(cls := "post-month") 
      e
    })

  private def onDayClick(d: LocalDate): Unit =
    close()
    dateSelectedPublisher.publish(d)

