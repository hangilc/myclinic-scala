package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.model.AppEvent
import dev.myclinic.scala.web.appoint.AppointHelper.catchall
import dev.myclinic.scala.web.appoint.Events
import dev.myclinic.scala.web.appoint.Events.ModelEvent
import dev.myclinic.scala.webclient.Api

import scala.concurrent.Future
import scala.scalajs.js

import scalajs.js.timers.setTimeout
import scalajs.js.timers.SetTimeoutHandle
import concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

class GlobalEventLinearizer(var nextEventId: Int, handler: AppEvent => Unit) {
  private val queue = js.Array[Work]()
  private var currentWorker: Option[SetTimeoutHandle] = None

  def post(appEvent: AppEvent): Unit = {
    queue.push(EventWork(appEvent))
    startWorker()
  }

  private def startWorker(): Unit = {
    if (currentWorker.isEmpty && !queue.isEmpty) {
      currentWorker = Some(createWorker(queue.shift()))
    }
  }

  def createWorker(work: Work): SetTimeoutHandle = setTimeout(0) {
    catchall(work.process()).onComplete(result => {
      result match {
        case Success(_) =>
        case Failure(e) => {
          System.err.println(e)
          queue.unshift(work)
        }
      }
      currentWorker = None
      startWorker()
    })
  }

  private sealed trait Work {
    def process(): Future[Unit]
  }

  private case class EventWork(appEvent: AppEvent) extends Work {
    def process(): Future[Unit] = {
      println("EventWork", nextEventId, appEvent)
      if (nextEventId == appEvent.eventId) {
        nextEventId += 1
        handler(appEvent)
        Future.successful(())
      } else {
        def f(events: List[AppEvent]): Unit = {
          queue.unshift(this)
          queue.unshift(RangeWork(events, appEvent.eventId))
        }

        for {
          events <- Api.listAppEventInRange(nextEventId, appEvent.eventId - 1)
        } yield f(events)
      }
    }
  }

  private case class RangeWork(events: List[AppEvent], nextId: Int)
      extends Work {
    def process(): Future[Unit] = {
      println("RangeWork", events, nextId, nextEventId)
      events.foreach(handler(_))
      nextEventId = nextId
      Future.successful(())
    }
  }

}

object GlobalEventDispatcher {
  val listeners = js.Array[GlobalEventListener]()

  def createListener(process: ModelEvent => Unit): GlobalEventListener = {
    val listener = new GlobalEventListener(process)
    addListener(listener)
    listener
  }

  def dispatch(appEvent: AppEvent): Unit = {
    val me = Events.convert(appEvent)
    listeners.foreach(_.post(me))
    listeners.foreach(_.drain())
  }

  def addListener(listener: GlobalEventListener): Unit = {
    listeners.push(listener)
  }

  def removeListener(listener: GlobalEventListener): Unit = {
    val len = listeners.length
    listeners -= listener
    assert(listeners.length == len - 1)
  }

}

class GlobalEventListener(val process: ModelEvent => Unit) {
  private val queue = js.Array[ModelEvent]()

  private var enabled: Boolean = false

  def enable(): Unit = {
    enabled = true
    drain()
  }

  def disable(): Unit = {
    enabled = false
  }

  def post(event: ModelEvent): Unit = {
    queue.push(event)
  }

  def drain(): Unit = {
    while (!queue.isEmpty && enabled) {
      val e = queue.shift()
      process(e)
    }
  }

  def dispose(): Unit = {
    disable()
    GlobalEventDispatcher.removeListener(this)
  }

  def suspending(proc: => Future[Unit]): Future[Unit] = {
    val enabledSave = enabled

    disable()
    for {
      _ <- catchall(proc)
    } yield { enabled = enabledSave }
  }

}
