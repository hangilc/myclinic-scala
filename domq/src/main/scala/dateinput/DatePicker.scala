package dev.fujiwara.domq.dateinput

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{Absolute}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import scala.collection.mutable.ListBuffer
import dev.fujiwara.domq.dateinput.datepicker.*
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import dev.fujiwara.kanjidate.KanjiDate.Era
import dev.fujiwara.kanjidate.KanjiDate
import java.time.DayOfWeek
import dev.fujiwara.kanjidate.DateUtil
import dev.fujiwara.kanjidate.DateUtil.given

case class DatePicker(init: Option[LocalDate])(
  using suggest: DateInput.Suggest
):
  private val dateSelectedPublisher = new LocalEventPublisher[LocalDate]
  private val initSuggest: Option[LocalDate] =
    init match {
      case None => suggest.value
      case Some(_) => None
    }
  private val setupDate: LocalDate = init.orElse(initSuggest).getOrElse(LocalDate.now())
  private val initDay: Option[Int] = init.map(_.getDayOfMonth)
  private val suggestDay: Option[Int] = initSuggest.map(_.getDayOfMonth)
  val yearDisp = YearDisp(setupDate.getYear)
  val monthDisp = MonthDisp(setupDate.getMonthValue)
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
  monthDisp.onIncMonth(doIncMonth _)
  Absolute.enableDrag(ele, hand)
  var close: () => Unit = () => ()

  def onDateSelected(handler: LocalDate => Unit): Unit =
    dateSelectedPublisher.subscribe(handler)

  def open(locator: HTMLElement => Unit): Unit =
    var cur: LocalDate = setupDate
    stuffDates(cur.getYear, cur.getMonthValue)
    close = Absolute.openWithScreen(ele, locator)

  private def doChange(newYear: Int, newMonth: Int): Unit =
    if yearDisp.year != newYear then yearDisp.simulateChange(newYear)
    if monthDisp.month != newMonth then monthDisp.simulateChange(newMonth)
    stuffDates(newYear, newMonth)

  private def doChangeYear(newYear: Int): Unit =
    doChange(newYear, monthDisp.month)

  private def doIncMonth(n: Int): Unit =
    val newM: LocalDate = LocalDate.of(yearDisp.year, monthDisp.month, 1).plusMonths(n)
    doChange(newM.getYear, newM.getMonthValue)

  private def doChangeMonth(newMonth: Int): Unit =
    doChange(yearDisp.year, newMonth)

  private def adjustedDay(year: Int, month: Int, day: Int): Int =
    if day <= 28 then day
    else
      val lastDay = DateUtil.lastDayOfMonth(year, month).getDayOfMonth
      day.min(lastDay)

  private enum CalDate(date: LocalDate):
    def value: LocalDate = date
    case PreDate(date: LocalDate) extends CalDate(date)
    case MonthDate(date: LocalDate) extends CalDate(date)
    case PostDate(date: LocalDate) extends CalDate(date)

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
    datesTab(clear, children := makeDates(year, month))

  private def makeDates(year: Int, month: Int): List[HTMLElement] =
    import CalDate.*
    val calDates: List[CalDate] = listCalendarDates(year, month)
    val initDayEquiv: Option[Int] = initDay.map(adjustedDay(year, month, _))
    val suggestDayEquiv: Option[Int] = suggestDay.map(adjustedDay(year, month, _))
    calDates.map(c => {
      val d = c.value
      val e = div(d.getDayOfMonth.toString)(
        cls := "domq-date-picker-date-box",
        onclick := (() => onDayClick(d)),
        attr("data-date") := d.getDayOfMonth.toString
      )
      d.getDayOfWeek match 
        case DayOfWeek.SUNDAY => e(cls := "sunday")
        case DayOfWeek.SATURDAY => e(cls := "saturday")
        case _ => ()
      c match {
        case _: PreDate =>
          e(cls := "pre-month")
        case _: MonthDate => 
          if Some(d) == init then e(cls := "init-date")
          else if Some(d) == initSuggest then e(cls := "suggest-date")
          else 
            if Some(d.getDayOfMonth) == initDayEquiv then
              e(cls := "init-date-equiv")
            else if Some(d.getDayOfMonth) == suggestDayEquiv then
              e(cls := "suggest-date-equiv")
        case _: PostDate =>
          e(cls := "post-month")
      }
      e
    })

  private def onDayClick(d: LocalDate): Unit =
    close()
    dateSelectedPublisher.publish(d)

