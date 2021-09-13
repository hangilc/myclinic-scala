package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import org.scalajs.dom
import org.scalajs.dom.document
import java.time.LocalDate
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.MyclinicApi
import scala.language.implicitConversions

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

  def openWebSocket(): Future[Unit] = ???

  // def openWebSocket(): Future[Unit] = {
  //   def f(nextEventId: Int): Unit = {
  //     val linear = new GlobalEventLinearizer(nextEventId, e => {
  //       GlobalEventDispatcher.dispatch(e)
  //     })
  //     val location = dom.window.location
  //     val origProtocol = location.protocol
  //     val host = location.host
  //     val protocol = origProtocol match {
  //       case "https:" => "wss:"
  //       case _        => "ws:"
  //     }
  //     val url = s"${protocol}//${host}/ws/events"
  //     val ws = new dom.WebSocket(url)
  //     ws.onmessage = { (e: dom.raw.MessageEvent) =>
  //       {
  //         val src = e.data.asInstanceOf[String]
  //         println("message", src)
  //         val appEvent: AppEvent = Api.fromJson[AppEvent](src)
  //         linear.post(appEvent)
  //       }
  //     }
  //   }

  //   for {
  //     nextEventId <- Api.getNextEventId(())
  //   } yield f(nextEventId)
  // }

}
