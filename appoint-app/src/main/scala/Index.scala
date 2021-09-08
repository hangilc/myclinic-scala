package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.appoint.MakeAppointDialog
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import java.time.LocalDate
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.Failure
import scala.util.Success
import org.scalajs.dom.raw.CustomEvent
import org.scalajs.dom

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    body(cls := "px-5 pt-1 pb-5")
    body.appendChild(banner)
    openWebSocket()
     val workarea = div()
    body.appendChild(workarea)
    val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    val endDate = startDate.plusDays(6)
    AppointSheet.setupDateRange(startDate, endDate)
    AppointSheet.setupTo(workarea)
  }

  val banner = div(cls := "container-fluid")(
    div(cls := "row pt-3 pb-2 ml-5 mr-5")(
      h1(cls := "bg-dark text-white p-3 col-md-12")("診察予約")
    )
  )

  def openWebSocket(): Unit = {
    val location = dom.window.location
    val origProtocol = location.protocol
    val host = location.host
    val protocol = origProtocol match {
      case "https:" => "wss:"
      case _        => "ws:"
    }
    val url = s"${protocol}//${host}/ws/events"
    val ws = new dom.WebSocket(url)
    ws.onmessage = {
      (e: dom.raw.MessageEvent) => {
        val src = e.data.asInstanceOf[String]
        val appEvent: AppEvent = Api.fromJson[AppEvent](src)
        appEvent.model match {
          case "appoint" => {
            val data = Api.fromJson[Appoint](appEvent.data)
            println(appEvent.model, appEvent.kind, data)
          }
          case _ =>
        }
        println("appEvent", appEvent)
      }
    }
  }

}
