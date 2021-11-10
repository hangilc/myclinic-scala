package dev.myclinic.scala.event

import dev.myclinic.scala.model.*
import scala.collection.mutable

trait ModelEventSubscriberController:
  def start(): Unit
  def stop(): Unit

case class ModelEventSubscriber[T <: AppModelEvent](private val handler: T => Unit)
    extends dev.myclinic.scala.event.ModelEventSubscriberController:
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

case class ModelEventPublisher[T <: AppModelEvent](
    var subscribers: Set[ModelEventSubscriber[T]] =
      Set.empty[ModelEventSubscriber[T]]
):
  def subscribe(handler: T => Unit): ModelEventSubscriber[T] =
    val sub = ModelEventSubscriber(handler)
    subscribers = subscribers + sub
    sub

  def publish(event: T): Unit =
    println(("publishing", event))
    subscribers.foreach(_.handle(event))

object ModelEventPublishers:
  val appointCreated = ModelEventPublisher[AppointCreated]()
  val appointUpdated = ModelEventPublisher[AppointUpdated]()
  val appointDeleted = ModelEventPublisher[AppointDeleted]()
  val appointTimeCreated = ModelEventPublisher[AppointTimeCreated]()
  val appointTimeUpdated = ModelEventPublisher[AppointTimeUpdated]()
  val appointTimeDeleted = ModelEventPublisher[AppointTimeDeleted]()

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
