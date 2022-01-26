package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable

trait EventSubscriberController:
  def start(): Unit
  def stop(): Unit

case class EventSubscriber[T <: AppModelEvent](
    private val handler: (T, AppEvent) => Unit,
    publisher: EventPublisher[T]
) extends EventSubscriberController:
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
    var subscribers: Set[EventSubscriber[T]] = Set.empty[EventSubscriber[T]]
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

case class RealTimeEventPublisher[T](
    var handlers: Set[T => Unit] = Set.empty[T => Unit]
):
  def addHandler(handler: T => Unit): Unit =
    handlers = handlers + handler

  def publish(event: T): Unit = handlers.foreach(_(event))

class ModelPublishers[
    C <: AppModelEvent,
    U <: AppModelEvent,
    D <: AppModelEvent
]:
  val created = EventPublisher[C]()
  val updated = EventPublisher[U]()
  val deleted = EventPublisher[D]()

class EventPublishers:
  val appoint = ModelPublishers[AppointCreated, AppointUpdated, AppointDeleted]
  val appointTime =
    ModelPublishers[AppointTimeCreated, AppointTimeUpdated, AppointTimeDeleted]
  val wqueue = ModelPublishers[WqueueCreated, WqueueUpdated, WqueueDeleted]
  val shahokokuho = ModelPublishers[ShahokokuhoCreated, ShahokokuhoUpdated, ShahokokuhoDeleted]
  val hotlineCreated = EventPublisher[HotlineCreated]()
  val hotlineBeep = RealTimeEventPublisher[HotlineBeep]()

class EventDispatcher extends EventPublishers:
  def publish(event: AppModelEvent, raw: AppEvent): Unit =
    event match {
      case e: AppointCreated     => appoint.created.publish(e, raw)
      case e: AppointUpdated     => appoint.updated.publish(e, raw)
      case e: AppointDeleted     => appoint.deleted.publish(e, raw)
      case e: AppointTimeCreated     => appointTime.created.publish(e, raw)
      case e: AppointTimeUpdated     => appointTime.updated.publish(e, raw)
      case e: AppointTimeDeleted     => appointTime.deleted.publish(e, raw)
      case e: WqueueCreated     => wqueue.created.publish(e, raw)
      case e: WqueueUpdated     => wqueue.updated.publish(e, raw)
      case e: WqueueDeleted     => wqueue.deleted.publish(e, raw)
      case e: HotlineCreated     => hotlineCreated.publish(e, raw)
      case _                     => ()
    }
  def publish(event: HotlineBeep): Unit = hotlineBeep.publish(event)
