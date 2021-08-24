package dev.myclinic.web

import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.{Api, Dialog, Tmpl, DomUtil}
import dev.myclinic.scala.web.Modifiers._
import dev.myclinic.scala.web.Implicits._
import dev.myclinic.scala.web.html._
import io.circe.parser.decode
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.experimental.URL
import org.scalajs.dom.experimental.URLSearchParams
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.Element

import java.time.LocalDate
import java.time.LocalTime
import scala.concurrent._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.Dictionary
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
    div(cls := "row mx-0", cb := { e => { eRow = e }})
  )

  def add(c: AppointColumn) {
    eRow.appendChild(c.ele)
  }
}

case class AppointColumn(appointDate: AppointDate) {
  
  var eDateRep, eSlots: Element = _
  val ele = div(cls := "col-2")(
    div(cb := (eDateRep = _)),
    div(cb := (eSlots = _)),
  )

  eDateRep.innerText = dateRep
  appointDate.appoints.foreach(a => {
    val s = new SlotRow(a)
    eSlots.appendChild(s.ele)
  })

  def dateRep: String = {
    val d: LocalDate = appointDate.date
    val month = d.getMonthValue()
    val day = d.getDayOfMonth()
    s"${month}月${day}日"
  }
}

case class SlotRow(appoint: Appoint) {

  var eTime, eDetail: Element = _
  val ele = div(style := "cursor: pointer")(
    div(cb := (eTime = _)),
    div(cb := (eDetail = _)),
  )

  eTime.innerText = appoint.time.toString()
  eDetail.innerText = detail
  ele.onclick(openDialog _)
  
  def detail: String = {
    if (appoint.patientName.isEmpty) {
      "（空）"
    } else {
      appoint.patientName
    }
  }

  def openDialog(): Unit = {
    val dlog = Dialog.create("診察予約")
    var eOk, eCancel: Element = null
    dlog.commandBox(
      button(cb := (eOk = _), attr("type") := "button", cls := "btn btn-outline-primary")("ＯＫ"),
      button(cb := (eCancel = _), attr("type") := "button", cls := "btn btn-outline-secondary ms-2")("キャンセル"),
    )
    eCancel.onclick(() => dlog.cancel())
    dlog.open()
  }

}
