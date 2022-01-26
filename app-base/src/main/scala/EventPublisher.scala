package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable

trait EventSubscriberController:
  def start(): Unit
  def stop(): Unit

case class EventSubscriber[T <: AppModelEvent](private val handler: (T, AppEvent) => Unit,
  publisher: EventPublisher[T])
    extends EventSubscriberController:
  var isStopped: Boolean = true
  val queue = mutable.Queue[(T, AppEvent)]()

  def handle(event: T, appEvent: AppEvent): Unit =
    queue.append((event, appEvent))
    if !isStopped then handleQueue()

  def start(): Unit =
    isStopped = false
    handleQueue()

  def stop(): Unit =
    isStopped = true

  def unsubscribe(): Unit =
    publisher.unsubscribe(this)

  private def handleQueue(): Unit =
    while !queue.isEmpty do
      val (event, appEvent) = queue.dequeue()
      handleOne(event, appEvent)

  private def handleOne(event: T, appEvent: AppEvent): Unit =
    try handler(event, appEvent)
    catch {
      case e: Throwable => System.err.println(e.toString)
    }

case class EventPublisher[T <: AppModelEvent](
    var subscribers: Set[EventSubscriber[T]] =
      Set.empty[EventSubscriber[T]]
):
  def subscribe(handler: (T, AppEvent) => Unit): EventSubscriber[T] =
    val sub = EventSubscriber(handler, this)
    subscribers = subscribers + sub
    sub

  def subscribe(handler: T => Unit): EventSubscriber[T] =
    subscribe((t, _) => handler(t))

  def publish(event: T, appEvent: AppEvent): Unit =
    subscribers.foreach(_.handle(event, appEvent))

  def unsubscribe(subscriber: EventSubscriber[T]): Unit =
    subscribers = subscribers - subscriber

class EventPublishers:
  val appointCreated = EventPublisher[AppointCreated]()
  val appointUpdated = EventPublisher[AppointUpdated]()
  val appointDeleted = EventPublisher[AppointDeleted]()
  val appointTimeCreated = EventPublisher[AppointTimeCreated]()
  val appointTimeUpdated = EventPublisher[AppointTimeUpdated]()
  val appointTimeDeleted = EventPublisher[AppointTimeDeleted]()
  val hotlineCreated = EventPublisher[HotlineCreated]()
  val wqueueCreated = EventPublisher[WqueueCreated]()
  val wqueueUpdated = EventPublisher[WqueueUpdated]()
  val wqueueDeleted = EventPublisher[WqueueDeleted]()

class EventDispatcher extends EventPublishers:
  def publish(event: AppModelEvent, raw: AppEvent): Unit =
    event match {
      case e: AppointCreated     => appointCreated.publish(e, raw)
      case e: AppointUpdated     => appointUpdated.publish(e, raw)
      case e: AppointDeleted     => appointDeleted.publish(e, raw)
      case e: AppointTimeCreated => appointTimeCreated.publish(e, raw)
      case e: AppointTimeUpdated => appointTimeUpdated.publish(e, raw)
      case e: AppointTimeDeleted => appointTimeDeleted.publish(e, raw)
      case e: HotlineCreated => hotlineCreated.publish(e, raw)
      case e: WqueueCreated => wqueueCreated.publish(e, raw)
      case e: WqueueUpdated => wqueueUpdated.publish(e, raw)
      case e: WqueueDeleted => wqueueDeleted.publish(e, raw)
      case _                     => ()
    }
