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
import dev.myclinic.scala.event.ModelEvents.{AppointUpdated, AppointCreated, AppointDeleted}
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.web.appoint.Removing
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appointTime.sheet.MakeAppointDialog
import cats.syntax.all._
import cats.implicits._
import cats.Monoid

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

    val ele = div(cls := "container px-0 mx-0")(
      div(cls := "row mx-0", bindTo(rowBinding))
    )

    def init(
        appointTimes: List[AppointTime],
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): Unit =
      clear()
      val appointDates = AppointDate.classify(appointTimes, appointMap)
      columns = appointDates.map(AppointColumn.create(_, appointMap)).toList
      columns.foreach(addElement _)

    def clear(): Unit =
      rowBinding.element.clear()

    def addElement(col: AppointColumn): Unit =
      rowBinding.element(col.ele)

  case class AppointColumn(
      date: LocalDate,
      appointMap: Map[AppointTimeId, List[Appoint]]
  ):
    var boxBinding = ElementBinding()
    val boxes: Array[AppointTimeBox] = Array()
    val ele = div(cls := "col-2")(
      div(dateRep),
      div(bindTo(boxBinding))
    )

    def dateRep: String = Misc.formatAppointDate(date)

    def setAppointTimes(appointTimes: List[AppointTime]): Unit =
      appointTimes.map(a => {
        val box = AppointTimeBox(a, appointMap.getOrElse(a.appointTimeId, List.empty))
        boxes :+ box
        boxBinding.element(box.ele)
      })

  object AppointColumn:
    def create(
        appointDate: AppointDate,
        appointMap: Map[AppointTimeId, List[Appoint]]
    ): AppointColumn =
      val c = AppointColumn(appointDate.date, appointMap)
      c.setAppointTimes(appointDate.appointTimes)
      c

  case class AppointTimeBox(
      appointTime: AppointTime,
      appoints: List[Appoint]
  ):
    val slotsElement = ElementBinding()
    val ele =
      div(style := "cursor: pointer", onclick := (onEleClick _))
        (
          div(timeLabel),
          div(bindTo(slotsElement))
        )
    
    appoints.foreach(app => {
      slotsElement.element(makeSlot(app))
    })

    def makeSlot(appoint: Appoint): Element = 
      div(appoint.patientName)

    def timeLabel: String =
      val f = Misc.formatAppointTime(appointTime.fromTime)
      val u = Misc.formatAppointTime(appointTime.untilTime)
      s"$f - $u"

    def onEleClick(): Unit =
      MakeAppointDialog.open(
        appointTime,
        name => {
          val app = Appoint(0, 0, appointTime.appointTimeId, name, 0, "")
          Api.registerAppoint(app)
        }
      )

// var slots: Array[SlotRow] =
//   appointDate.appointTimes.map(app => SlotRow(app, this)).toArray

// slots.foreach(s => slotsBinding.element(s.ele))

// def replaceSlotBy(prev: SlotRow, slot: SlotRow): Unit =
//   val index = slots.indexOf(prev)
//   if index >= 0 then
//     slots(index) = slot
//     Removing.broadcastRemoving(prev.ele)
//     prev.ele.replaceBy(slot.ele)

// case class SlotRow(appointTime: AppointTime, col: AppointColumn):

//   val ele = div(style := "cursor: pointer", onclick := (onEleClick _))(
//     div(Misc.formatAppointTime(appointTime.fromTime)),
//     div(detail)
//   )

// val modelEventHandler: ModelEvent => Unit = (_: @unchecked) match {
//   case AppointUpdated(updated) =>
//     if appoint.requireUpdate(updated) then
//       val newSlot = SlotRow(updated, col)
//       col.replaceSlotBy(this, newSlot)
// }
// ModelEventDispatcher.addHandler(modelEventHandler)
// Removing.addRemovingListener(
//   ele,
//   () => ModelEventDispatcher.removeHandler(modelEventHandler)
// )

// def detail: String =
//   if appoint.patientName.isEmpty then "（空）"
//   else appoint.patientName

// def onEleClick(): Unit =
//   if appoint.isVacant then openMakeAppointDialog()
//   else openCancelAppointDialog()

// def openMakeAppointDialog(): Unit =
//   MakeAppointDialog.open(
//     appoint,
//     name => {
//       Api
//         .registerAppoint(
//           Appoint(appoint.date, appoint.time, 0, name, 0, "")
//         )
//         .onComplete[Unit](_ match {
//           case Success(_)         => println("Success")
//           case Failure(exception) => println(("failure", exception))
//         })
//     }
//   )

// def openCancelAppointDialog(): Unit =
//   CancelAppointDialog.open(appoint)
