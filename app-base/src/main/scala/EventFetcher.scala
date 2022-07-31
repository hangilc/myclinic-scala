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
import dev.myclinic.scala.model.{HotlineBeep, EventIdNotice, HeartBeat}
import java.time.LocalDateTime
import org.scalajs.dom.WebSocket
import org.scalajs.dom.CloseEvent
import dev.fujiwara.domq.LocalEventPublisher
import scala.util.Try
import scala.scalajs.js.timers

class EventFetcher:
  val appModelEventPublisher: LocalEventPublisher[AppModelEvent] =
    LocalEventPublisher[AppModelEvent]
  val hotlineBeepEventPublisher: LocalEventPublisher[HotlineBeep] =
    LocalEventPublisher[HotlineBeep]

  private var events: Vector[AppModelEvent] = Vector.empty

  def catchup(lastExcludedEventId: Int, f: AppModelEvent => Unit): Unit =
    val i = events.lastIndexWhere(_.appEventId <= lastExcludedEventId)
    events.slice(i + 1, events.size).foreach(f)

  private def onNewAppEvent(event: AppModelEvent): Unit =
    events = events :+ event
    appModelEventPublisher.publish(event)

  var nextEventId: Int = 0
  var wsOpt: Option[WebSocket] = None
  private var isDraining = false
  private var retryConnectTimeout = 1

  def start(): Future[Unit] =
    for nextEventIdValue <- Api.getNextAppEventId()
    yield
      nextEventId = nextEventIdValue
      connect()

  private def connect(): Unit =
    if wsOpt.isEmpty then
      println("Websocket connecting ...")
      Try {
        val ws = new dom.WebSocket(url)
        println(("ws", ws))
        println("Websocket created")
        ws.onopen = { (e: dom.Event) =>
          println("Websocket opened")
          retryConnectTimeout = 1
          if wsOpt.isEmpty then
            wsOpt = Some(ws)
            ws.onclose = (e: dom.CloseEvent) => {
              println("Websocket closed")
              wsOpt = None
              retryConnectTimeout = 1
              timers.setTimeout(retryConnectTimeout * 1000)(connect())
            }
          else ws.close(1000, "no use")
        }
        ws.onmessage = { (e: dom.MessageEvent) =>
          {
            val msg = e.data.asInstanceOf[String]
            println(("ws-message", msg))
            if !isDraining then handleMessage(msg)
          }
        }
        ws.onerror = { (e: dom.ErrorEvent) =>
          println("Websocket error")
          retryConnectTimeout = (retryConnectTimeout * 2).min(15)
          timers.setTimeout(retryConnectTimeout * 1000)(connect())
        }
      } match {
        case Success(_) => ()
        case Failure(_) =>
          retryConnectTimeout = (retryConnectTimeout * 2).min(15)
          timers.setTimeout(retryConnectTimeout * 1000)(connect())
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
    decode[EventType](msg) match {
      case Right(event) =>
        event match {
          case appEvent @ _: AppEvent =>
            handleAppEvent(appEvent)
            println(("app-event", msg))
          case hotlineBeep @ _: HotlineBeep =>
            hotlineBeepEventPublisher.publish(hotlineBeep)
          case eventIdNotice: EventIdNotice =>
            if eventIdNotice.currentEventId >= nextEventId then
              drainEvents()
              println(("drained", eventIdNotice.currentEventId))
          case _: HeartBeat =>
            println("heart-beat")
            wsOpt.foreach(ws => ws.send("heart-beat"))
            ()
        }
      case Left(ex) => System.err.println(ex.getMessage)
    }

  private def drainEvents(): Unit =
    isDraining = true
    val op =
      for events <- Api.listAppEventSince(nextEventId)
      yield events.foreach(event => {
        val modelEvent = AppModelEvent.from(event)
        if event.appEventId >= nextEventId then
          nextEventId = event.appEventId + 1
          onNewAppEvent(modelEvent)
      })
    op.onComplete(r =>
      isDraining = false
      r match {
        case Success(_)  => ()
        case Failure(ex) => System.err.println(ex.getMessage)
      }
    )

  private def handleAppEvent(appEvent: AppEvent): Unit =
    if isRelevant(appEvent) then
      val modelEvent = AppModelEvent.from(appEvent)
      if appEvent.appEventId == nextEventId then
        onNewAppEvent(modelEvent)
        nextEventId += 1
      else if appEvent.appEventId > nextEventId then drainEvents()
