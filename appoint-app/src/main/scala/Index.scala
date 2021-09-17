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
    {
      val client = div("Listener")
      body(client)
      EventSystem.addListener(modelEvent => println(modelEvent), client)
      val client2 = div("Listener-2")
      body(client2)
      EventSystem.addListener(modelEvent => println(modelEvent), client2)
      val app = Appoint(LocalDate.now(), java.time.LocalTime.now(), 1, "henry", 0, "")
      val modelEvent = Events.AppointCreated(app)
      EventSystem.dispatch(modelEvent, body)
      body.removeChild(client)
      EventSystem.dispatch(modelEvent, body)
    }

  val banner = div(cls := "container-fluid")(
    div(cls := "row pt-3 pb-2 ml-5 mr-5")(
      h1(cls := "bg-dark text-white p-3 col-md-12")("診察予約")
    )
  )

  val eventListeners = js.Array[EventListener]()

  def addEventListener(listener: EventListener): Unit =
    eventListeners.push(listener)

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
              HandleEventAction.enqueue(appEvent)
              nextEventId += 1
            else
              val action = TarckMissingEventsAction(
                nextEventId,
                appEvent.eventId,
                appEvent
              )
              QueueRunner.enqueue(action)
              nextEventId = appEvent.eventId + 1
          case Left(ex) => System.err.println(ex.toString())
      }

    for nextEventId <- Api.getNextAppEventId()
    yield f(nextEventId)

trait EventListener:
  def handleEvent(event: Events.ModelEvent): Unit

case class HandleEventAction(listener: EventListener, event: Events.ModelEvent)
    extends QueueRunner.Action:
  def start(): Future[Unit] = Future.successful(listener.handleEvent(event))
  def onComplete(result: Boolean): Unit = ()

object HandleEventAction:
  def enqueue(appEvent: AppEvent): Unit =
    val modelEvent = Events.convert(appEvent)
    val actions = JsMain.eventListeners.map(HandleEventAction(_, modelEvent))
    actions.foreach(QueueRunner.enqueue(_))

case class TarckMissingEventsAction(
    startEventId: Int,
    untilEventId: Int,
    following: AppEvent
) extends QueueRunner.Action:
  def start(): Future[Unit] =
    for events <- Api.listAppEventInRange(startEventId, untilEventId)
    yield
      events.foreach(HandleEventAction.enqueue(_))
      HandleEventAction.enqueue(following)

  def onComplete(result: Boolean): Unit = ()
