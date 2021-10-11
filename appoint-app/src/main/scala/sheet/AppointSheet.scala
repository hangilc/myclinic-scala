package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
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
import dev.myclinic.scala.event.ModelEventPublishers
import dev.myclinic.scala.event.ModelEventSubscriberController
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.web.appoint.Misc
import cats.syntax.all._
import cats.implicits._
import cats.Monoid
import dev.myclinic.scala.event.getMaxEventId

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
    var columns: List[AppointColumn] = List()
    var appointTimes: List[AppointTime] = List.empty

    val ele = columnWrapper(
      display := "flex",
      justifyContent := "center"
    )

    val subscribers: List[ModelEventSubscriberController] = List(
      ModelEventPublishers.appointCreated.subscribe(onAppointCreated),
      ModelEventPublishers.appointDeleted.subscribe(onAppointDeleted),
      ModelEventPublishers.appointTimeUpdated.subscribe(onAppointTimeUpdated),
      ModelEventPublishers.appointTimeDeleted.subscribe(onAppointTimeDeleted),
    )

    def init(
        appointTimes: List[AppointTime],
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): Unit =
      subscribers.foreach(_.stop())
      val maxEventId = getMaxEventId(
        appointTimes :: appointMap.values.toList: _*
      )
      clear()
      AppointRow.appointTimes = appointTimes
      val appointDates = AppointDate.classify(appointTimes, appointMap)
      columns = appointDates
        .map(AppointColumn.create(_, appointMap, makeAppointTimeBox))
        .toList
      columns.foreach(addElement)
      subscribers.foreach(_.start(maxEventId))

    def clear(): Unit =
      columnWrapper.clear()

    def addElement(col: AppointColumn): Unit =
      columnWrapper(col.ele(margin := "0 0.5rem"))

    private def propagateToColumn(
        appoint: Appoint,
        f: (AppointColumn, Appoint) => Unit
    ): Unit =
      appointTimes
        .find(at => at.appointTimeId == appoint.appointTimeId)
        .flatMap(at => columns.find(c => c.date == at.date))
        .map(c => f(c, appoint))

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

    def onAppointTimeDeleted(event: AppointTimeDeleted): Unit =
      findColumnByDate(event.deleted.date).map(col =>
        col.deleteAppointTime(event.deleted.appointTimeId)
      )

  def makeAppointTimeBox(
      appointTime: AppointTime,
      appoints: List[Appoint]
  ): AppointTimeBox =
    AppointTimeBox(appointTime, appoints)

case class AppointDate(
    date: LocalDate,
    appointTimes: List[AppointTime],
    appointMap: Map[AppointDate.AppointTimeId, List[Appoint]]
)

object AppointDate:
  type AppointTimeId = Int
  def classify(
      appList: List[AppointTime],
      appointMap: Map[AppointTimeId, List[Appoint]]
  ): List[AppointDate] =
    val map = appList.groupBy(_.date)
    val result = for k <- map.keys yield AppointDate(k, map(k), appointMap)
    result.toList.sortBy(_.date)
