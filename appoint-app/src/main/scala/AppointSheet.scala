package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.myclinic.scala.model._
import org.scalajs.dom.raw.Element

import java.time.LocalDate
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.Failure
import scala.util.Success
import dev.myclinic.scala.web.appoint.Events._
import scala.concurrent.Future
import dev.myclinic.scala.webclient.Api
import scala.language.implicitConversions

object AppointSheet {
  val eles = div(TopMenu.ele, AppointRow.ele)
  var dateRange: Option[(LocalDate, LocalDate)] = None
  val listener: GlobalEventListener = GlobalEventDispatcher.createListener({
    case AppointUpdated(app) => AppointRow.respondToUpdatedEvent(app)
    case _                   =>
  })
  listener.enable()

  def setupDateRange(from: LocalDate, upto: LocalDate): Future[Unit] = {
    println(("setupDateRange", from, upto))
    listener.suspending {
      for {
        appoints <- Api.listAppoint(from, upto)
        _ = AppointRow.init(appoints)
        _ = { dateRange = Some((from, upto)) }
      } yield ()
    }
  }

  def setupTo(wrapper: Element): Unit = {
    wrapper(eles)
  }

  case class AppointDate(date: LocalDate, appoints: List[Appoint])

  object AppointDate {
    def classify(appList: List[Appoint]): List[AppointDate] = {
      val map = appList.groupBy(_.date)
      val result = for (k <- map.keys) yield AppointDate(k, map(k))
      result.toList.sortBy(_.date)
    }
  }

  object TopMenu {
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

    def onPrevWeek(): Future[Unit] = {
      println(("dateRange", dateRange))
      dateRange match {
        case Some((from, upto)) => {
          val fromNext = from.plusDays(-7)
          val uptoNext = upto.plusDays(-7)
          setupDateRange(fromNext, uptoNext)
        }
        case None => Future.successful(())
      }
    }

    def onNextWeek(): Future[Unit] = {
      dateRange match {
        case Some((from, upto)) => {
          val fromNext = from.plusDays(7)
          val uptoNext = upto.plusDays(7)
          setupDateRange(fromNext, uptoNext)
        }
        case None => Future.successful(())
      }
    }
  }

  object AppointRow {

    var rowBinding = ElementBinding()
    var columns: List[AppointColumn] = List()

    val ele = div(cls := "container px-0 mx-0")(
      div(cls := "row mx-0", bindTo(rowBinding))
    )

    def init(appoints: List[Appoint]): Unit = {
      clear()
      val appointDates = AppointDate.classify(appoints)
      columns = appointDates.map(AppointColumn(_)).toList
      columns.foreach(addElement _)
    }

    def clear(): Unit = {
      rowBinding.element.clear()
    }

    def addElement(col: AppointColumn): Unit = {
      rowBinding.element(col.ele)
    }

    def respondToUpdatedEvent(updated: Appoint): Unit = {
      columns.foreach(_.respondToUpdatedEvent(updated))
    }

  }

  case class AppointColumn(appointDate: AppointDate) {

    val date = appointDate.date
    var slotsBinding = ElementBinding()
    val ele = div(cls := "col-2")(
      div(dateRep),
      div(bindTo(slotsBinding))
    )
    var slots: Array[SlotRow] =
      appointDate.appoints.map(app => SlotRow(app, this)).toArray

    def respondToUpdatedEvent(updated: Appoint): Unit = {
      slots.foreach(_.respondToUpdatedEvent(updated))
    }

    def dateRep: String = Misc.formatAppointDate(date)
    slots.foreach(s => slotsBinding.element(s.ele))

    def replaceSlotBy(prev: SlotRow, slot: SlotRow): Unit = {
      val index = slots.indexOf(prev)
      if (index >= 0) {
        slots(index) = slot
        prev.ele.replaceBy(slot.ele)
      }
    }
  }

  case class SlotRow(appoint: Appoint, col: AppointColumn) {

    val ele = div(style := "cursor: pointer", onclick := (onEleClick _))(
      div(Misc.formatAppointTime(appoint.time)),
      div(detail)
    )

    def respondToUpdatedEvent(updated: Appoint): Unit = {
      if (appoint.requireUpdate(updated)) {
        val newSlot = SlotRow(updated, col)
        col.replaceSlotBy(this, newSlot)
      }
    }

    def detail: String = {
      if (appoint.patientName.isEmpty) {
        "（空）"
      } else {
        appoint.patientName
      }
    }

    def onEleClick(): Unit = {
      if (appoint.isVacant) {
        openMakeAppointDialog()
      } else {
        openCancelAppointDialog()
      }
    }

    def openMakeAppointDialog(): Unit = {
      MakeAppointDialog.open(
        appoint,
        name => {
          Api
            .registerAppoint(Appoint(appoint.date, appoint.time, 0, name, 0, ""))
            .onComplete[Unit](_ match {
              case Success(_)         => println("Success")
              case Failure(exception) => println(("failure", exception))
            })
        }
      )
    }

    def openCancelAppointDialog(): Unit = {
      CancelAppointDialog.open(appoint)
    }
  }
}
