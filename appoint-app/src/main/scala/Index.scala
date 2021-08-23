package dev.myclinic.web

import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.{Api, Dialog, Tmpl, DomUtil}
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
    List("px-5", "pt-1", "pb-5").foreach(body.classList.add)
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

  val bannerTmpl = """
    <div class="container-fluid">
        <div class="row pt-3 pb-2 ml-5 mr-5">
            <h1 class="bg-dark text-white p-3 col-md-12">診察予約</h1>
        </div>
    </div>
    <div class="x-hello">hello</div>
    """

  val banner = Tmpl.createElement(bannerTmpl)

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
  val tmpl = """"
    <div class="container px-0 mx-0">
      <div class="row mx-0 x-row"></div>
    </div>
  """

  val ele = Tmpl.createElement(tmpl)
  var eRow: Element = _

  DomUtil.traversex(ele, (name, e) => {
    name match {
      case "row" => eRow = e
      case _ =>
    }
  })

  def add(c: AppointColumn) {
    eRow.appendChild(c.ele)
  }
}

case class AppointColumn(appointDate: AppointDate) {
  val tmpl = """
    <div class="col-2">
      <div class="x-date-rep"></div>
      <div class="x-slots"></div>
    </div>
  """
  
  val ele = Tmpl.createElement(tmpl)
  var eDateRep, eSlots: Element = _

  DomUtil.traversex(ele, (name, e) => {
    name match {
      case "date-rep" => eDateRep = e
      case "slots" => eSlots = e
      case _ =>
    }
  })
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
  val tmpl = """
    <div style="cursor: pointer">
      <div class="x-time"></div>
      <div class="x-detail"></div>
    </div>
  """

  val ele = Tmpl.createElement(tmpl)
  var eTime, eDetail: Element = _

  DomUtil.traversex(ele, (name, e) => {
    name match {
      case "time" => eTime = e
      case "detail" => eDetail = e
      case _ =>
    }
  })
  eTime.innerText = appoint.time.toString()
  eDetail.innerText = detail
  DomUtil.onClick(ele, openDialog)
  
  def detail: String = {
    if (appoint.patientName.isEmpty) {
      "（空）"
    } else {
      appoint.patientName
    }
  }

  def openDialog(): Unit = {
    val dlog = Dialog.create("診察予約")
    dlog.open()
  }

}
