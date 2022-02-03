package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{AppEvent, AppModelEvent}
import org.scalajs.dom
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.Api
import scala.language.implicitConversions
import io.circe.*
import io.circe.syntax.*
import io.circe.parser.decode
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.model.jsoncodec.EventType
import dev.myclinic.scala.model.HotlineBeep

abstract class EventFetcher:
  def publish(event: AppModelEvent, appEventId: Int): Unit = ()
  def publish(event: HotlineBeep): Unit = ()

  private var events: Vector[(Int, AppModelEvent)] = Vector.empty

  def catchup(baseEventId: Int, f: (Int, AppModelEvent) => Unit): Unit =
    val i = events.lastIndexWhere(_._1 <= baseEventId)
    events.slice(i+1, events.size).foreach((gen, m) => f(gen, m))

  private def onNewAppEvent(event: AppModelEvent, eventId: Int): Unit =
    events = events :+ (eventId, event)
    publish(event, eventId)

  var nextEventId: Int = 0
  def start(): Future[Unit] =
    for nextEventIdValue <- Api.getNextAppEventId()
    yield {
      nextEventId = nextEventIdValue
      val ws = new dom.WebSocket(url)
      ws.onmessage = { (e: dom.raw.MessageEvent) =>
        {
          val msg = e.data.asInstanceOf[String]
          handleMessage(msg)
        }
      }
    }

  def isRelevant(appEvent: AppEvent): Boolean = true

  private def url: String =
    val location = dom.window.location
    val origProtocol = location.protocol
    val host = location.host
    val protocol = origProtocol match
      case "https:" => "wss:"
      case _        => "ws:"
    s"${protocol}//${host}/ws/events"

  private def handleMessage(msg: String): Unit =
    println(("message", msg))
    decode[EventType](msg) match {
      case Right(event) => 
        event match {
          case appEvent @ _: AppEvent => handleAppEvent(appEvent)
          case hotlineBeep @ _: HotlineBeep => publish(hotlineBeep)
        }
      case Left(ex)        => System.err.println(ex.getMessage)
    }

  private def handleAppEvent(appEvent: AppEvent): Unit =
    if isRelevant(appEvent) then
      val modelEvent = AppModelEvent.from(appEvent)
      if appEvent.appEventId == nextEventId then
        onNewAppEvent(modelEvent, appEvent.appEventId)
        nextEventId += 1
      else if appEvent.appEventId > nextEventId then
        Api
          .listAppEventInRange(nextEventId, appEvent.appEventId)
          .onComplete({
            case Success(events) =>
              val modelEvents = events.map(raw => (AppModelEvent.from(raw), raw))
              modelEvents.foreach((event, raw) => onNewAppEvent(event, raw.appEventId))
              onNewAppEvent(modelEvent, appEvent.appEventId)
              nextEventId = appEvent.appEventId + 1
            case Failure(ex) => System.err.println(ex.getMessage)
          })


