package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable

trait EventSubscriberController:
  def start(): Unit
  def stop(): Unit

case class EventSubscriber[T <: AppModelEvent](private val handler: T => Unit)
    extends EventSubscriberController:
  var isStopped: Boolean = true
  val queue = mutable.Queue[T]()

  def handle(event: T): Unit =
    if isStopped then queue.append(event)
    else
      queue.append(event)
      handleQueue()

  def start(): Unit =
    isStopped = false
    handleQueue()

  def stop(): Unit =
    isStopped = true

  private def handleQueue(): Unit =
    while !queue.isEmpty do
      val event = queue.dequeue()
      handleOne(event)

  private def handleOne(event: T): Unit =
    try handler(event)
    catch {
      case e: Throwable => System.err.println(e.toString)
    }

case class EventPublisher[T <: AppModelEvent](
    var subscribers: Set[EventSubscriber[T]] =
      Set.empty[EventSubscriber[T]]
):
  def subscribe(handler: T => Unit): EventSubscriber[T] =
    val sub = EventSubscriber(handler)
    subscribers = subscribers + sub
    sub

  def publish(event: T): Unit =
    subscribers.foreach(_.handle(event))

class EventPublishers:
  val appointCreated = EventPublisher[AppointCreated]()
  val appointUpdated = EventPublisher[AppointUpdated]()
  val appointDeleted = EventPublisher[AppointDeleted]()
  val appointTimeCreated = EventPublisher[AppointTimeCreated]()
  val appointTimeUpdated = EventPublisher[AppointTimeUpdated]()
  val appointTimeDeleted = EventPublisher[AppointTimeDeleted]()

class EventDispatcher extends EventPublishers:
  def publish(event: AppModelEvent): Unit =
    event match {
      case e: AppointCreated     => appointCreated.publish(e)
      case e: AppointUpdated     => appointUpdated.publish(e)
      case e: AppointDeleted     => appointDeleted.publish(e)
      case e: AppointTimeCreated => appointTimeCreated.publish(e)
      case e: AppointTimeUpdated => appointTimeUpdated.publish(e)
      case e: AppointTimeDeleted => appointTimeDeleted.publish(e)
      case _                     => ()
    }
