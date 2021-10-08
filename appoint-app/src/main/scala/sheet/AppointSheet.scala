package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import org.scalajs.dom.raw.Element
import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.collection.mutable
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.event.ModelEvents.ModelEvent
import dev.myclinic.scala.event.ModelEvents.{
  AppointUpdated,
  AppointCreated,
  AppointDeleted
}
import dev.myclinic.scala.event.ModelEventPublishers
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.web.appoint.Misc
import cats.syntax.all._
import cats.implicits._
import cats.Monoid
import dev.myclinic.scala.event.getMaxEventId

object AppointSheet:
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

  def setupTo(wrapper: Element): Unit =
    wrapper(eles)

  case class AppointDate(
      date: LocalDate,
      appointTimes: List[AppointTime],
      appointMap: Map[AppointTimeId, List[Appoint]]
  )

  object AppointDate:
    def classify(
        appList: List[AppointTime],
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): List[AppointDate] =
      val map = appList.groupBy(_.date)
      val result = for k <- map.keys yield AppointDate(k, map(k), appointMap)
      result.toList.sortBy(_.date)

  object TopMenu:
    val prevWeekBinding, nextWeekBinding = ElementBinding()
    val ele = div(cls := "mb-2")(
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

    prevWeekBinding.element.onclick(() => onPrevWeek())
    nextWeekBinding.element.onclick(() => onNextWeek())

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

    var rowBinding = ElementBinding()
    var columns: List[AppointColumn] = List()
    var appointTimes: List[AppointTime] = List.empty

    val ele = div(cls := "container px-0 mx-0")(
      div(cls := "row mx-0", bindTo(rowBinding))
    )

    val appointCreatedSubscriber =
      ModelEventPublishers.appointCreated.subscribe(onAppointCreated)
    val appointDeletedSubscriber =
      ModelEventPublishers.appointDeleted.subscribe(onAppointDeleted)

    def init(
        appointTimes: List[AppointTime],
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): Unit =
      val subscribers = List(appointCreatedSubscriber, appointDeletedSubscriber)
      subscribers.foreach(_.stop())
      val maxEventId = getMaxEventId(appointTimes :: appointMap.values.toList: _*)
      clear()
      AppointRow.appointTimes = appointTimes
      val appointDates = AppointDate.classify(appointTimes, appointMap)
      columns = appointDates.map(AppointColumn.create(_, appointMap)).toList
      columns.foreach(addElement)
      subscribers.foreach(_.start(maxEventId))
      

    def clear(): Unit =
      rowBinding.element.clear()

    def addElement(col: AppointColumn): Unit =
      rowBinding.element(col.ele)

    private def propagateToColumn(appoint: Appoint, f: (AppointColumn, Appoint) => Unit): Unit =
      appointTimes
        .find(at => at.appointTimeId == appoint.appointTimeId)
        .flatMap(at => columns.find(c => c.date == at.date))
        .map(c => f(c, appoint))

    def onAppointCreated(event: AppointCreated): Unit =
      propagateToColumn(event.created, _.insert(_))

    def onAppointDeleted(event: AppointDeleted): Unit =
      propagateToColumn(event.deleted, _.delete(_))

  case class AppointColumn(
      date: LocalDate,
      appointMap: Map[AppointTimeId, List[Appoint]]
  ):
    var boxBinding = ElementBinding()
    var boxes: Array[AppointTimeBox] = Array()
    val ele = div(cls := "col-2")(
      div(dateRep),
      div(bindTo(boxBinding))
    )

    def dateRep: String = Misc.formatAppointDate(date)

    def setAppointTimes(appointTimes: List[AppointTime]): Unit =
      appointTimes.map(a => {
        val box =
          AppointTimeBox(a, appointMap.getOrElse(a.appointTimeId, List.empty))
        boxes = boxes :+ box
        boxBinding.element(box.ele)
      })

    private def findBoxByAppoint(appoint: Appoint): Option[AppointTimeBox] =
      boxes.find(b => b.appointTime.appointTimeId == appoint.appointTimeId)
    
    def insert(appoint: Appoint): Unit =
      findBoxByAppoint(appoint).map(_.addAppoint(appoint))

    def delete(appoint: Appoint): Unit =
      findBoxByAppoint(appoint).map(_.removeAppoint(appoint))

  object AppointColumn:
    def create(
        appointDate: AppointDate,
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): AppointColumn =
      val c = AppointColumn(appointDate.date, appointMap)
      c.setAppointTimes(appointDate.appointTimes)
      c

  