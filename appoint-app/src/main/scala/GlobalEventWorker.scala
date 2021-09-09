package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.model.AppEvent
import scalajs.js.timers.setTimeout
import scalajs.js.timers.SetTimeoutHandle
import scala.scalajs.js
import scala.scalajs.js.JavaScriptException
import dev.myclinic.scala.webclient.Api
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future

object GlobalEventWorker {
  var nextEventId = 0
  var queue = js.Array[Work]()
  var currentWorker: Option[SetTimeoutHandle] = None

  sealed trait Work {
    def process(): Future[Unit]
  }

  case class EventWork(appEvent: AppEvent) extends Work {
    def process(): Future[Unit] = {
      if (nextEventId == appEvent.eventId) {
        Future.successful(Events.handle(appEvent))
      } else {
        def f(events: List[AppEvent]): Unit = {
          queue.unshift(this)
          queue.unshift(RangeWork(events, appEvent.eventId))
        }

        for {
          events <- Api.listAppEventInRange(nextEventId, appEvent.eventId - 1)
          _ = f(events)
        } yield ()
      }
    }
  }

  case class RangeWork(events: List[AppEvent], nextId: Int) extends Work {
    def process(): Unit = {
      events.foreach(Events.handle(_))
      nextEventId = nextId
    }
  }

  def createWorker(work: Work): SetTimeoutHandle = setTimeout(0) {
    try {
      work.process()
    } catch {
      case JavaScriptException(e) => {
        System.err.println(e.toString())
      }
      case e: Throwable => {
        System.err.println(e.toString)
      }
    }
    startWorker()
  }

  def startWorker(): Unit = {
    if (currentWorker.isEmpty && !queue.isEmpty) {
      currentWorker = Some(createWorker(queue.shift()))
    }
  }

  def postEvent(appEvent: AppEvent): Unit = {
    queue.push(EventWork(appEvent))
    startWorker()
  }
}
