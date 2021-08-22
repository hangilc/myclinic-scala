package dev.myclinic.web

import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.{Api, Dialog}
import io.circe.parser.decode
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.experimental.URL
import org.scalajs.dom.experimental.URLSearchParams
import org.scalajs.dom.ext.Ajax

import java.time.LocalDate
import java.time.LocalTime
import scala.concurrent._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.Dictionary
import scala.util.Failure
import scala.util.Success
import scalatags.JsDom.all._

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    List("px-5", "pt-1", "pb-5").foreach(body.classList.add)
    body.appendChild(banner)
    body.appendChild(AppointRow.dom)
    val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    val endDate = startDate.plusDays(6)
    val api = Api
    for (apps <- api.listAppoint(startDate, endDate)) {
      val cols = AppointDate.classify(apps).map(AppointColumn)
      cols.foreach(AppointRow.add)
    }
  }

  val banner = div(cls := "container-fluid")(
    div(cls := "row pt-3 pb-2 ml-5 mr-5")(
      h1(cls := "bg-dark text-white p-3 col-md-12")("診察予約")
    )
  ).render

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
  val row = div(cls := "row mx-0").render
  val dom = div(cls := "container px-0 mx-0")(row).render

  def add(c: AppointColumn) {
    row.appendChild(c.dom)
  }
}

case class AppointColumn(appointDate: AppointDate) {
  val dom = div(cls := "col-2")(dateRep).render
  appointDate.appoints.foreach(app => {
    val r = AppointTimeRow(app)
    dom.appendChild(r.dom)
  })

  def dateRep: String = {
    val d: LocalDate = appointDate.date
    val month = d.getMonthValue()
    val day = d.getDayOfMonth()
    s"${month}月${day}日"
  }
}

case class AppointTimeRow(appoint: Appoint) {
  val datePart = div(appoint.time.toString)
  val detailPart = div(
    detail
  )
  val dom = div(
    onclick := { () => openDialog },
    style := "cursor: pointer"
  ) (datePart, detailPart).render

  def detail: String = {
    if (appoint.patientName.isEmpty) {
      "（空）"
    } else {
      appoint.patientName
    }
  }

  def openDialog = {
    Dialog.open("TEST")
  }

}
