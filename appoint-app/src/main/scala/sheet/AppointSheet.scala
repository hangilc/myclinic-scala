package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import math.Ordered.orderingToOrdered
import org.scalajs.dom.raw.HTMLElement
import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.collection.mutable
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.event.ModelEvents.ModelEvent
import dev.myclinic.scala.event.ModelEvents.*
import dev.myclinic.scala.event.{ModelEventPublishers => Pub}
import dev.myclinic.scala.event.ModelEventSubscriberController
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.web.appoint.Misc
import cats.syntax.all._
import cats.implicits._
import cats.Monoid

class AppointSheet:
  val eles = div(TopMenu.ele, AppointRow.ele)
  var dateRange: Option[(LocalDate, LocalDate)] = None
  type AppointTimeId = Int

  def setupDateRange(from: LocalDate, upto: LocalDate): Future[Unit] =
    val dates = DateUtil.enumDates(from, upto)
    var appointList: List[List[Appoint]] = List.empty
    for
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
    yield
      AppointRow.init(appointTimes, makeAppointMap(appointList.flatten))
      dateRange = Some((from, upto))

  def makeAppointMap(
      appoints: List[Appoint]
  ): Map[AppointTimeId, List[Appoint]] =
    Monoid[Map[AppointTimeId, List[Appoint]]].combineAll(
      appoints.map(app => Map(app.appointTimeId -> List(app)))
    )

  def setupTo(wrapper: HTMLElement): Unit =
    wrapper(eles)

  def dateRangeIncludes(date: LocalDate): Boolean =
    println(("dateRangeIncludes", dateRange, date))
    dateRange match {
      case Some(from, upto) => from <= date && date <= upto
      case None             => false
    }

  object TopMenu:
    val prevWeekBinding, nextWeekBinding = ElementBinding()
    val ele = div(
      mb := "0.5rem",
      css(style => {
        style.textAlign = "center"
      })
    )(
      button(
        attr("type") := "button",
        cls := "btn btn-outline-primary",
        bindTo(prevWeekBinding),
        "前の週"
      ),
      button(
        attr("type") := "button",
        cls := "btn btn-outline-primary ms-2",
        bindTo(nextWeekBinding),
        "次の週"
      )
    )

    prevWeekBinding.element(onclick := (() => onPrevWeek()))
    nextWeekBinding.element(onclick := (() => onNextWeek()))

    def onPrevWeek(): Unit =
      dateRange match
        case Some((from, upto)) => {
          val fromNext = from.plusDays(-7)
          val uptoNext = upto.plusDays(-7)
          setupDateRange(fromNext, uptoNext)
        }
        case None => Future.successful(())

    def onNextWeek(): Unit =
      dateRange match
        case Some((from, upto)) =>
          val fromNext = from.plusDays(7)
          val uptoNext = upto.plusDays(7)
          setupDateRange(fromNext, uptoNext)
        case None => Future.successful(())

  object AppointRow:

    var columnWrapper: HTMLElement = div()
    var columns: Seq[AppointColumn] = List.empty

    val ele = columnWrapper(
      display := "flex",
      justifyContent := "center"
    )

    val subscribers: List[ModelEventSubscriberController] = List(
      Pub.appointCreated.subscribe(onAppointCreated),
      Pub.appointDeleted.subscribe(onAppointDeleted),
      Pub.appointTimeCreated.subscribe(onAppointTimeCreated),
      Pub.appointTimeUpdated.subscribe(onAppointTimeUpdated),
      Pub.appointTimeDeleted.subscribe(onAppointTimeDeleted)
    )

    def init(
        appointTimes: List[AppointTime],
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): Unit =
      subscribers.foreach(_.stop())
      clear()
      val dates: List[AppointDate] =
        AppointDate.classify(appointTimes, appointMap)
      dates
        .foreach((date: AppointDate) => {
          val list: List[(AppointTime, List[Appoint])] =
            date.appointTimes.map(appointTime =>
              (
                appointTime,
                appointMap.get(appointTime.appointTimeId).getOrElse(List.empty)
              )
            )
          addColumn(AppointColumn.create(date.date, list, makeAppointTimeBox))
        })
      subscribers.foreach(_.start())

    def clear(): Unit =
      columnWrapper.clear()
      columns = List.empty

    def addColumn(col: AppointColumn): Unit =
      col.ele(margin := "0 0.5rem")
      columns = sortedAppointColumn.insert(col, columns, columnWrapper)

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
      propagateToColumn(event.created, _.insert(_))

    def onAppointDeleted(event: AppointDeleted): Unit =
      propagateToColumn(event.deleted, _.delete(_))

    def onAppointTimeUpdated(event: AppointTimeUpdated): Unit =
      findColumnByDate(event.updated.date).map(col =>
        col.updateAppointTime(event.updated)
      )

    def onAppointTimeCreated(event: AppointTimeCreated): Unit =
      val date = event.created.date
      findColumnByDate(date)
        .orElse {
          if dateRangeIncludes(date) then
            val c = AppointColumn(date, makeAppointTimeBox)
            addColumn(c)
            println(("column-added", c))
            Some(c)
          else None
        }
        .foreach(c => c.addAppointTime(event.created))

    def onAppointTimeDeleted(event: AppointTimeDeleted): Unit =
      findColumnByDate(event.deleted.date).map(col =>
        col.deleteAppointTime(event.deleted.appointTimeId)
      )

  def makeAppointTimeBox(
      appointTime: AppointTime
  ): AppointTimeBox =
    AppointTimeBox(appointTime)

case class AppointDate(
    date: LocalDate,
    appointTimes: List[AppointTime]
)

object AppointDate:
  type AppointTimeId = Int
  def classify(
      appList: List[AppointTime],
      appointMap: Map[AppointTimeId, List[Appoint]]
  ): List[AppointDate] =
    val map = appList.groupBy(_.date)
    val result =
      for k <- map.keys yield AppointDate(k, map(k).sortBy(_.fromTime))
    result.toList.sortBy(_.date)
