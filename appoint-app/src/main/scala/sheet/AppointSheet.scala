package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.all.{given, *}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.clinicop.*
import dev.myclinic.scala.util.DateUtil
import math.Ordered.orderingToOrdered
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.collection.mutable
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appbase.EventPublishers
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint.history.History
import cats.syntax.all._
import cats.implicits._
import cats.Monoid
import dev.myclinic.scala.web.appoint.{AppointHistoryWindow, CustomEvents}
import dev.myclinic.scala.web.appoint.sheet.covidthirdshot.CovidThirdShot
import dev.myclinic.scala.clinicop.{NationalHoliday, RegularHoliday}
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.web.appbase.EventFetcher

class AppointSheet(using EventFetcher):
  import AppointSheet.*
  val topMenu = new TopMenu(startDayOfWeek)
  val columnsWrapper = div
  val ele = div(
    topMenu.ele,
    columnsWrapper(display := "flex", justifyContent := "center")
  )
  topMenu.onDateSelected.subscribe(setup _)
  setup(topMenu.getStartDate)
  ele.addCreatedListener[AppointTime](event => {
    val g = event.appEventId
    val created = event.dataAs[AppointTime]
    val date = created.date
    val seltor = s".appoint-column.date-${date}"
    ele
      .qSelector(seltor)
      .foreach(e => {
        CustomEvents.appointTimeCreated.trigger(e, (g, created), false)
      })
  })
  ele.addCreatedListener[Appoint](event => {
    val g = event.appEventId
    val created: Appoint = event.dataAs[Appoint]
    val seltor = s".appoint-time-box.appoint-time-id-${created.appointTimeId}"
    ele
      .qSelector(seltor)
      .foreach(e => {
        CustomEvents.appointCreated.trigger(e, (g, created), false)
      })
  })

  def setup(startDate: LocalDate): Unit =
    for workDays <- listWorkingDays(startDate)
    yield
      columnsWrapper(clear)
      workDays.foreach { case (date, op) =>
        val col = makeAppointColumn(date, op)
        col.init
        columnsWrapper(col.ele)
      }

  def makeAppointColumn(date: LocalDate, op: ClinicOperation): AppointColumn =
    AppointColumn(date, op)

object AppointSheet:
  def startDayOfWeek(at: LocalDate): LocalDate = DateUtil.startDayOfWeek(at)
  def startDayOfWeek: LocalDate = startDayOfWeek(LocalDate.now())
  def isWorkingDay(op: ClinicOperation): Boolean =
    op match {
      case _: RegularHoliday => false
      case _                 => true
    }
  def listWorkingDays(
      dates: List[LocalDate]
  ): Future[List[(LocalDate, ClinicOperation)]] =
    for clinicOpMap <- Api.batchResolveClinicOperations(dates)
    yield dates
      .map(date =>
        val op = clinicOpMap(date)
        if isWorkingDay(op) then Some(date, op) else None
      )
      .flatten
  def listWorkingDays(
      startDate: LocalDate
  ): Future[List[(LocalDate, ClinicOperation)]] =
    val dates = DateUtil.enumDates(startDate, startDate.plusDays(6))
    listWorkingDays(dates)

class AppointSheetOrig(using EventFetcher):
  val daySpanDisp: HTMLElement = div(css(style => {
    style.display = "none"
    style.textAlign = "center"
    style.padding = "1rem 0"
  }))
  val eles = div(TopMenu.ele, daySpanDisp, AppointRow.ele)

  var dateRange: Option[(LocalDate, LocalDate)] = None
  type AppointTimeId = Int

  def setupDateRange(from: LocalDate, upto: LocalDate): Unit =
    val dates = DateUtil.enumDates(from, upto)
    for
      clinicOpMap <- Api.batchResolveClinicOperations(dates)
      cols = filterDates(dates, clinicOpMap).map((date, op) =>
        makeAppointColumn(date, op)
      )
      _ = AppointRow.init(cols)
    yield dateRange = Some(from, upto)

  def filterDates(
      dates: List[LocalDate],
      opMap: Map[LocalDate, ClinicOperation]
  ): List[(LocalDate, ClinicOperation)] =
    dates
      .map(date => (date, opMap(date)))
      .filter((_, op) =>
        op match {
          case _: RegularHoliday => false
          case _                 => true
        }
      )

  def makeAppointColumn(date: LocalDate, op: ClinicOperation): AppointColumn =
    new AppointColumn(date, op)

  def showDaySpanDisp(): Unit =
    dateRange match {
      case Some(a, b) => {
        val at = Misc.formatAppointDate(a)
        val bt = Misc.formatAppointDate(b)
        val txt = s"${at} - ${bt}"
        daySpanDisp(clear)
        daySpanDisp(span(txt), displayDefault)
      }
      case None => hideDaySpanDisp()
    }

  def hideDaySpanDisp(): Unit =
    daySpanDisp.innerHTML = ""
    daySpanDisp(displayNone)

  def makeAppointMap(
      appoints: List[Appoint]
  ): Map[AppointTimeId, List[Appoint]] =
    Monoid[Map[AppointTimeId, List[Appoint]]].combineAll(
      appoints.map(app => Map(app.appointTimeId -> List(app)))
    )

  def setupTo(wrapper: HTMLElement): Unit =
    wrapper(eles)

  def dateRangeIncludes(date: LocalDate): Boolean =
    dateRange match {
      case Some(from, upto) => from <= date && date <= upto
      case None             => false
    }

  object TopMenu:
    val topMenuBox: HTMLElement = span()
    val ele = div(
      mb := "0.5rem",
      css(style => {
        style.textAlign = "center"
      })
    )(
      button("前の月", onclick := (() => advanceDays(-28))),
      button("前の週", onclick := (() => advanceDays(-7))),
      a("今週", href := "", ml := "0.5rem", onclick := (onThisWeekClick _)),
      button("次の週", ml := "0.5rem", onclick := (() => advanceDays(7))),
      button("次の月", ml := "0.5rem", onclick := (() => advanceDays(28))),
      topMenuBox(attr("id") := "top-menu-box")(
        a("追加接種", mr := "14px", onclick := (onThirdShotClick _)),
        Icons.menu(Icons.defaultStyle, onclick := (onMenuClick _))
      )
    )

    def onThirdShotClick(): Unit =
      val content = CovidThirdShot()
      val w =
        FloatWindow("追加接種", content.ui.ele(padding := "10px"), width = "300px")
      w.open()
      content.initFocus()

    def onMenuClick(event: MouseEvent): Unit =
      ContextMenu(List("変更履歴" -> (showHistory _))).open(event)

    def showHistory(): Unit =
      val f =
        for
          events <- Api.listAppointEvents(30, 0)
          _ <- AppointHistoryWindow.open(events)
        yield ()
      f.onComplete {
        case Success(_)  => ()
        case Failure(ex) => ShowMessage.showError(ex.getMessage)
      }

    def advanceDays(days: Int): Unit =
      dateRange match
        case Some((from, upto)) => {
          val fromNext = from.plusDays(days)
          val uptoNext = upto.plusDays(days)
          setupDateRange(fromNext, uptoNext)
        }
        case None => ()

    def onThisWeekClick(): Unit =
      val start = DateUtil.startDayOfWeek(LocalDate.now())
      val end = start.plusDays(6)
      setupDateRange(start, end)

  object AppointRow:

    var columnWrapper: HTMLElement = div()
    // var columns: Seq[AppointColumn] = List.empty

    val ele = columnWrapper(
      display := "flex",
      justifyContent := "center"
    )

    def init(cols: List[AppointColumn]): Unit =
      columnWrapper(clear, children := cols.map(_.ele))
