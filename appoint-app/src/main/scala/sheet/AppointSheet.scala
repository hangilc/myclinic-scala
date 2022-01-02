package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.fujiwara.domq.{Icons, ContextMenu, FloatWindow, ShowMessage}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.clinicop.*
import dev.myclinic.scala.util.DateUtil
import math.Ordered.orderingToOrdered
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.collection.mutable
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.web.appbase.{
  EventPublishers,
  EventSubscriberController
}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.web.appoint.{Misc, GlobalEvents}
import dev.myclinic.scala.web.appoint.history.History
import cats.syntax.all._
import cats.implicits._
import cats.Monoid
import dev.myclinic.scala.web.appoint.AppointHistoryWindow

class AppointSheet(using eventPublishers: EventPublishers):
  val daySpanDisp: HTMLElement = div(css(style => {
    style.display = "none"
    style.textAlign = "center"
    style.padding = "1rem 0"
  }))
  val eles = div(TopMenu.ele, daySpanDisp, AppointRow.ele)
  var dateRange: Option[(LocalDate, LocalDate)] = None
  type AppointTimeId = Int

  def setupDateRange(from: LocalDate, upto: LocalDate): Future[Unit] =
    val dates = DateUtil.enumDates(from, upto)
    var appointList: List[List[Appoint]] = List.empty
    val f = for
      appointTimes <- Api.listAppointTimes(from, upto)
      _ <- dates
        .map(d => {
          for
            appoints <- Api.listAppointsForDate(d)
            _ = appointList = appoints :: appointList
          yield ()
        })
        .sequence
        .void
      clinicOpMap <- Api.batchResolveClinicOperations(dates)
    yield
      AppointRow.init(
        dates,
        appointTimes,
        makeAppointMap(appointList.flatten),
        clinicOpMap
      )
      dateRange = Some(from, upto)
      GlobalEvents.AppointColumnChanged.publish(AppointRow.columns)
    f transform (identity, ex => {
      System.err.println(ex)
      ex
    })

  GlobalEvents.AppointColumnChanged.subscribe(cols => {
    if cols.size > 0 then hideDaySpanDisp()
    else showDaySpanDisp()
  })

  def showDaySpanDisp(): Unit =
    dateRange match {
      case Some(a, b) => {
        val at = Misc.formatAppointDate(a)
        val bt = Misc.formatAppointDate(b)
        val txt = s"${at} - ${bt}"
        daySpanDisp.clear()
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

  def modifyColumn(col: AppointColumn): AppointColumn = col

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
        Icons.menu(Icons.defaultStyle, onclick := (onMenuClick _))
      )
    )

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
    var columns: Seq[AppointColumn] = List.empty

    val ele = columnWrapper(
      display := "flex",
      justifyContent := "center"
    )

    val subscribers: List[EventSubscriberController] = List(
      eventPublishers.appointCreated.subscribe(onAppointCreated),
      eventPublishers.appointUpdated.subscribe(onAppointUpdated),
      eventPublishers.appointDeleted.subscribe(onAppointDeleted),
      eventPublishers.appointTimeCreated.subscribe(onAppointTimeCreated),
      eventPublishers.appointTimeUpdated.subscribe(onAppointTimeUpdated),
      eventPublishers.appointTimeDeleted.subscribe(onAppointTimeDeleted)
    )

    def init(
        dates: List[LocalDate],
        appointTimes: List[AppointTime],
        appointMap: Map[AppointTimeId, List[Appoint]],
        clinicOpMap: Map[LocalDate, ClinicOperation]
    ): Unit =
      subscribers.foreach(_.stop())
      clear()
      val appointDates: List[AppointDate] =
        AppointDate.classify(dates, clinicOpMap, appointTimes, appointMap)
      appointDates
        .foreach((date: AppointDate) => {
          val list: List[(AppointTime, List[Appoint])] =
            date.appointTimes.map(appointTime =>
              (
                appointTime,
                appointMap.get(appointTime.appointTimeId).getOrElse(List.empty)
              )
            )
          addColumn(
            AppointColumn.create(date.date, date.op, list, makeAppointTimeBox)
          )
        })
      subscribers.foreach(_.start())

    def clear(): Unit =
      columnWrapper.clear()
      columns.foreach(_.ele.remove())
      columns = List.empty

    def addColumn(col: AppointColumn): Unit =
      col.ele(margin := "0 0.5rem")
      val modified = modifyColumn(col)
      columns = sortedAppointColumn.insert(modified, columns, columnWrapper)

    private def propagateToColumn(
        appoint: Appoint,
        f: (AppointColumn, Appoint) => Unit
    ): Unit =
      columns
        .find(c => c.hasAppointTimeId(appoint.appointTimeId))
        .foreach(c => f(c, appoint))

    private def findColumnByDate(date: LocalDate): Option[AppointColumn] =
      columns.find(c => c.date == date)

    def onAppointCreated(event: AppointCreated): Unit =
      propagateToColumn(event.created, _.addAppoint(_))

    def onAppointUpdated(event: AppointUpdated): Unit =
      propagateToColumn(
        event.updated,
        (c, a) => c.updateAppoint(a)
      )

    def onAppointDeleted(event: AppointDeleted): Unit =
      propagateToColumn(event.deleted, _.deleteAppoint(_))

    def onAppointTimeUpdated(event: AppointTimeUpdated): Unit =
      findColumnByDate(event.updated.date).map(col =>
        col.updateAppointTime(event.updated)
      )

    def onAppointTimeCreated(event: AppointTimeCreated): Unit =
      val date = event.created.date
      findColumnByDate(date)
        .foreach(c => {
          c.addAppointTime(event.created)
        })

    def onAppointTimeDeleted(event: AppointTimeDeleted): Unit =
      findColumnByDate(event.deleted.date).map(col =>
        col.deleteAppointTime(event.deleted.appointTimeId)
        GlobalEvents.AppointColumnChanged.publish(columns)
      )

  def makeAppointTimeBox(
      appointTime: AppointTime,
      followingVacantRegular: () => Option[AppointTime]
  ): AppointTimeBox =
    AppointTimeBox(appointTime, followingVacantRegular)

case class AppointDate(
    date: LocalDate,
    op: ClinicOperation,
    appointTimes: List[AppointTime]
)

object AppointDate:
  type AppointTimeId = Int
  def classify(
      dates: List[LocalDate],
      clinicOpMap: Map[LocalDate, ClinicOperation],
      appList: List[AppointTime],
      appointMap: Map[AppointTimeId, List[Appoint]]
  ): List[AppointDate] =
    val clinicDates: List[(LocalDate, ClinicOperation)] =
      dates
        .map(date => (date, clinicOpMap(date)))
        .filter(item =>
          item match {
            case (_, RegularHoliday()) => false
            case _                     => true
          }
        )
    val map = appList.groupBy(_.date)
    val result =
      for (date, op) <- clinicDates yield {
        AppointDate(
          date,
          op,
          map.getOrElse(date, List.empty).sortBy(_.fromTime)
        )
      }
    result.toList.sortBy(_.date)
