package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.model.AppEvent
import dev.myclinic.scala.webclient.Api

import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Failure
import scala.util.Success

import scalajs.js.timers.setTimeout
import scalajs.js.timers.SetTimeoutHandle
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.web.appoint.Events
import dev.myclinic.scala.web.appoint.Events.ModelEvent

object GlobalEventWorker {
  private var nextEventIdValue = 0
  private val queue = js.Array[Work]()
  private var currentWorker: Option[SetTimeoutHandle] = None
  private val listeners = js.Array[Listener]()
  private val history = js.Array[ModelEvent]()

  def nextEventId = nextEventIdValue

  private def nextEventId_=(value: Int): Unit = {
    nextEventIdValue = value
  }

  def initNextEventId(value: Int): Unit = {
    nextEventId = value
  }

  trait Listener {
    def handle(event: ModelEvent): Unit
  }

  def addListener(listener: Listener): Unit = {
    listeners.push(listener)
  }

  def trigger(appEvent: AppEvent): Unit = {
    val e = Events.convert(appEvent)
    history.push(e)
    listeners.foreach(listener => listener.handle(e))
  }

  def applyHistory(listener: Listener, startEventId: Int): Unit = {
    history.foreach(e => {
      if( e.eventId >= startEventId ){
        listener.handle(e)
      }
    })
  }

  sealed trait Work {
    def process(): Future[Unit]
  }

  case class EventWork(appEvent: AppEvent) extends Work {
    def process(): Future[Unit] = {
      println("EventWork", nextEventId, appEvent)
      if (nextEventId == appEvent.eventId) {
        nextEventId += 1
        trigger(appEvent)
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

  case class RangeWork(events: List[AppEvent], nextId: Int) extends Work {
    def process(): Future[Unit] = {
      println("RangeWork", events, nextId, nextEventId)
      events.foreach(trigger(_))
      nextEventId = nextId
      Future.successful(())
    }
  }

  def createWorker(work: Work): SetTimeoutHandle = setTimeout(0) {
    catchall(
      () => work.process(),
      (result: Either[Throwable, Unit]) =>
        result match {
          case Left(e) => {
            System.err.println(e)
            queue.unshift(work)
            currentWorker = None
            startWorker()
          }
          case Right(_) => {
            currentWorker = None
            startWorker()
          }
        }
    )
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

  private def catchall[A](
      f: () => Future[A],
      cb: Either[Throwable, A] => Unit
  ): Unit = {
    try {
      f().onComplete {
        case Success(value) => cb(Right(value))
        case Failure(e)     => cb(Left(e))
      }
    } catch {
      case (e: Throwable) => cb(Left(e))
    }
  }

}
