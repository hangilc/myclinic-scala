package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.all.{given, *}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.clinicop.*
import dev.fujiwara.kanjidate.DateUtil
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

