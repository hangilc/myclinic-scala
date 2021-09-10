package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.Binding._
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model._
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.raw.Element

import java.time.LocalDate
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.Failure
import scala.util.Success

object AppointSheet {
  val eles = div(AppointRow.ele)

  var dateRange: Option[(LocalDate, LocalDate)] = None

  def setupDateRange(from: LocalDate, upto: LocalDate): Unit = {
    def setup(appoints: List[Appoint]): Unit = {
      val appointDates = AppointDate.classify(appoints)
      val columns = appointDates.map(AppointColumn(_))
      AppointRow.set(columns)
      dateRange = Some((from, upto))
    }
    for {
      appoints <- Api.listAppoint(from, upto)
    } yield setup(appoints)
  }

  def setupTo(wrapper: Element): Unit = {
    wrapper(eles)
  }

  def onAppointUpdated(appoint: Appoint): Unit = {
    AppointRow.columns.foreach(c => {
      println("in onAppointUpdate", c.appointDate.date, appoint.date)
      if (c.appointDate.date == appoint.date) {
        c.updateRow(appoint)
      }
    })
  }

  case class AppointDate(date: LocalDate, appoints: List[Appoint])

  object AppointDate {
    def classify(appList: List[Appoint]): List[AppointDate] = {
      val map = appList.groupBy(_.date)
      val result = for (k <- map.keys) yield AppointDate(k, map(k))
      result.toList.sortBy(_.date)
    }
  }

  object AppointRow {

    var rowBinding = ElementBinding()
    var columns: List[AppointColumn] = List()

    val ele = div(cls := "container px-0 mx-0")(
      div(cls := "row mx-0", bindTo(rowBinding))
    )

    def clear(): Unit = {
      rowBinding.element.clear()
    }

    def set(cols: List[AppointColumn]): Unit = {
      columns = cols
      clear()
      columns.foreach(c => rowBinding.element(c.ele))
    }
  }

  case class AppointColumn(appointDate: AppointDate) {

    val date = appointDate.date
    var slotsBinding = ElementBinding()
    val ele = div(cls := "col-2")(
      div(dateRep),
      div(bindTo(slotsBinding))
    )
    var slots: Array[SlotRow] = appointDate.appoints.map(SlotRow(_)).toArray

    def dateRep: String = Misc.formatAppointDate(date)
    slots.foreach(s => slotsBinding.element(s.ele))

    def updateRow(app: Appoint): Unit = {
      for (i <- 0 until slots.length) {
        val slot = slots(i)
        if (slot.needUpdate(app)) {
          val newSlot = SlotRow(app)
          slot.ele.replaceBy(newSlot.ele)
          slots(i) = newSlot
        }
      }
    }
  }

  case class SlotRow(appoint: Appoint) {

    val listener = GlobalEventDispatcher.createListener(e => {
      e match {
        case Events.AppointUpdated(app)
            if app.sameDateTime(appoint) && app.eventId > appoint.eventId => {
              println("Need update")
            }
        case _ => 
      }
    })

    val ele = div(style := "cursor: pointer", onclick := (onEleClick _))(
      div(Misc.formatAppointTime(appoint.time)),
      div(detail)
    )

    listener.enable()

    def dispose(): Unit = {
      listener.dispose()
    }

    def needUpdate(newAppoint: Appoint): Boolean = {
      appoint.requireUpdate(newAppoint)
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
            .registerAppoint(appoint.date, appoint.time, name)
            .onComplete[Unit](_ match {
              case Success(_)         => println("Success")
              case Failure(exception) => println("failure", exception)
            })
        }
      )
    }

    def openCancelAppointDialog(): Unit = {
      CancelAppointDialog.open(appoint)
    }
  }
}
