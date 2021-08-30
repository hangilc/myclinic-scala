package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.util.KanjiDate
import dev.myclinic.scala.web.appoint.MakeAppointDialog
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import java.time.LocalDate
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.Failure
import scala.util.Success

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    body(cls := "px-5 pt-1 pb-5")
    body.appendChild(banner)
    body.appendChild(AppointRow.ele)
    val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    val endDate = startDate.plusDays(6)
    for (apps <- Api.listAppoint(startDate, endDate)) {
      val cols = AppointDate.classify(apps).map(AppointColumn)
      cols.foreach(AppointRow.add)
    }
  }

  val banner = div(cls := "container-fluid")(
    div(cls := "row pt-3 pb-2 ml-5 mr-5")(
      h1(cls := "bg-dark text-white p-3 col-md-12")("診察予約")
    )
  )

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

  var eRow: Element = _

  val ele = div(cls := "container px-0 mx-0")(
    div(cls := "row mx-0", cb := { e => { eRow = e } })
  )

  def add(c: AppointColumn): Unit = {
    eRow.appendChild(c.ele)
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
