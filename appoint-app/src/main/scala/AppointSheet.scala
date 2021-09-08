package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Binding._
import dev.myclinic.scala.model._
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.document
import org.scalajs.dom.raw.CustomEvent
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

  def handleEvent(event: AppEvent): Unit = ()

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

    def add(c: AppointColumn): Unit = {
      rowBinding.element.appendChild(c.ele)
    }

    def set(cols: List[AppointColumn]): Unit = {
      clear()
      cols.foreach(add _)
    }
  }

  
  case class AppointColumn(appointDate: AppointDate) {

    var eDateRep, eSlots: Element = _
    val ele = div(cls := "col-2")(
      div(cb := (eDateRep = _)),
      div(cb := (eSlots = _))
    )

    eDateRep.innerText = Misc.formatAppointDate(appointDate.date)
    appointDate.appoints.foreach(a => {
      val s = new SlotRow(a)
      eSlots.appendChild(s.ele)
    })

  }

  case class SlotRow(appoint: Appoint) {

    val ele = div(style := "cursor: pointer", onclick := (onEleClick _))(
      div(Misc.formatAppointTime(appoint.time)),
      div(detail)
    )

    document.body.addEventListener[CustomEvent](
      "mc-appoint-modified",
      e => {
        val modified = e.detail.asInstanceOf[Appoint]
        if (modified.date == appoint.date && modified.time == appoint.time) {
          println("modified", modified)
        }
      }
    )

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
