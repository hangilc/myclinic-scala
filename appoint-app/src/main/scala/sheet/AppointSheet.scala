package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model._
import org.scalajs.dom.raw.Element

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.web.appoint.Events.ModelEvent
import dev.myclinic.scala.web.appoint.Events.AppointUpdated
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.web.appoint.Removing
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint.ModelEventDispatcher

object AppointSheet:
  val eles = div(TopMenu.ele, AppointRow.ele)
  var dateRange: Option[(LocalDate, LocalDate)] = None

  def setupDateRange(from: LocalDate, upto: LocalDate): Future[Unit] =
    for appoints <- Api.listAppoint(from, upto)
    yield
      Removing.broadcastRemoving(eles)
      AppointRow.init(appoints)
      dateRange = Some((from, upto))

  def setupTo(wrapper: Element): Unit =
    wrapper(eles)

  case class AppointDate(date: LocalDate, appoints: List[Appoint])

  object AppointDate:
    def classify(appList: List[Appoint]): List[AppointDate] =
      val map = appList.groupBy(_.date)
      val result = for k <- map.keys yield AppointDate(k, map(k))
      result.toList.sortBy(_.date)

  object TopMenu:
    val prevWeekBinding, nextWeekBinding = ElementBinding()
    val ele = div(
      button(
        attr("type") := "button",
        cls := "btn btn-outline-primary",
        bindTo(prevWeekBinding),
        "前の週"
      ),
      button(
        attr("type") := "button",
        cls := "btn btn-outline-primary",
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

    def init(appoints: List[Appoint]): Unit =
      clear()
      val appointDates = AppointDate.classify(appoints)
      columns = appointDates.map(AppointColumn(_)).toList
      columns.foreach(addElement _)

    def clear(): Unit =
      rowBinding.element.clear()

    def addElement(col: AppointColumn): Unit =
      rowBinding.element(col.ele)

  case class AppointColumn(appointDate: AppointDate):

    val date = appointDate.date
    var slotsBinding = ElementBinding()
    val ele = div(cls := "col-2")(
      div(dateRep),
      div(bindTo(slotsBinding))
    )
    var slots: Array[SlotRow] =
      appointDate.appoints.map(app => SlotRow(app, this)).toArray

    def dateRep: String = Misc.formatAppointDate(date)
    slots.foreach(s => slotsBinding.element(s.ele))

    def replaceSlotBy(prev: SlotRow, slot: SlotRow): Unit =
      val index = slots.indexOf(prev)
      if index >= 0 then
        slots(index) = slot
        Removing.broadcastRemoving(prev.ele)
        prev.ele.replaceBy(slot.ele)

  case class SlotRow(appoint: Appoint, col: AppointColumn):

    val ele = div(style := "cursor: pointer", onclick := (onEleClick _))(
      div(Misc.formatAppointTime(appoint.time)),
      div(detail)
    )

    val modelEventHandler: ModelEvent => Unit = (_: @unchecked) match {
      case AppointUpdated(updated) =>
        if appoint.requireUpdate(updated) then
          val newSlot = SlotRow(updated, col)
          col.replaceSlotBy(this, newSlot)
    }
    ModelEventDispatcher.addHandler(modelEventHandler)
    Removing.addRemovingListener(
      ele,
      () => ModelEventDispatcher.removeHandler(modelEventHandler)
    )

    def detail: String =
      if appoint.patientName.isEmpty then "（空）"
      else appoint.patientName

    def onEleClick(): Unit =
      if appoint.isVacant then openMakeAppointDialog()
      else openCancelAppointDialog()

    def openMakeAppointDialog(): Unit =
      MakeAppointDialog.open(
        appoint,
        name => {
          Api
            .registerAppoint(
              Appoint(appoint.date, appoint.time, 0, name, 0, "")
            )
            .onComplete[Unit](_ match {
              case Success(_)         => println("Success")
              case Failure(exception) => println(("failure", exception))
            })
        }
      )

    def openCancelAppointDialog(): Unit =
      CancelAppointDialog.open(appoint)
