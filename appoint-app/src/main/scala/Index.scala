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
import scala.scalajs.js
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.{Api, UserError}
import scala.language.implicitConversions
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.modeljson.Implicits.{given}

object JsMain:
  def main(args: Array[String]): Unit =
    val body = document.body
    body(cls := "px-5 pt-1 pb-5")
    body.appendChild(banner)
    QueueRunner.enqueue(new QueueRunner.Action {
      def start(): Future[Unit] = openWebSocket()
      def onComplete(success: Boolean): Unit = ()
    })
    val workarea = div()
    body.appendChild(workarea)
    val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    val endDate = startDate.plusDays(6)
    AppointSheet.setupTo(workarea)
    QueueRunner.enqueue(new QueueRunner.Action:
      def start(): Future[Unit] =
        AppointSheet.setupDateRange(startDate, endDate)
      def onComplete(success: Boolean): Unit = ()
    )

  val banner = div(cls := "container-fluid")(
    div(cls := "row pt-3 pb-2 ml-5 mr-5")(
      h1(cls := "bg-dark text-white p-3 col-md-12")("診察予約")
    )
  )

  def openWebSocket(): Future[Unit] =
    def f(startEventId: Int): Unit =
      var nextEventId = startEventId
      val location = dom.window.location
      val origProtocol = location.protocol
      val host = location.host
      val protocol = origProtocol match
        case "https:" => "wss:"
        case _        => "ws:"
      val url = s"${protocol}//${host}/ws/events"
      val ws = new dom.WebSocket(url)
      ws.onmessage = { (e: dom.raw.MessageEvent) =>
        val src = e.data.asInstanceOf[String]
        println(("message", src))
        decode[AppEvent](src) match
          case Right(appEvent) =>
            val modelEvent = Events.convert(appEvent)
            if appEvent.eventId == nextEventId then
              ModelEventDispatcher.dispatch(modelEvent)
              nextEventId += 1
            else
              Api
                .listAppEventInRange(nextEventId, appEvent.eventId)
                .onComplete({
                  case Success(events) =>
                    val modelEvents = events.map(Events.convert(_))
                    modelEvents.foreach(ModelEventDispatcher.dispatch(_))
                    ModelEventDispatcher.dispatch(modelEvent)
                  case Failure(ex) => System.err.println(ex)
                })
          case Left(ex) => System.err.println(ex.toString())
      }

    for nextEventId <- Api.getNextAppEventId()
    yield f(nextEventId)
