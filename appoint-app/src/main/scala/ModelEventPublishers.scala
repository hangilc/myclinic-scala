package dev.myclinic.scala.event

import dev.myclinic.scala.event.ModelEvents.*
import scala.collection.mutable

case class ModelEventSubscriber[T <: ModelEvent](private val handler: T => Unit):
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

  def drainUpto(eventIdIncluded: Int): Unit =
    queue.dropWhileInPlace(e => e.eventId <= eventIdIncluded)

  private def handleQueue(): Unit =
    while !queue.isEmpty do
      val event = queue.dequeue()
      handleOne(event)

  private def handleOne(event: T): Unit = 
    try 
      handler(event)
    catch {
      case e: Throwable => System.err.println(e.toString)
    }

case class ModelEventPublisher[T <: ModelEvent](
    var subscribers: Set[ModelEventSubscriber[T]] = Set.empty[ModelEventSubscriber[T]]
):
  def subscribe(handler: T => Unit): ModelEventSubscriber[T] =
    val sub = ModelEventSubscriber(handler)
    subscribers = subscribers + sub
    sub

  def publish(event: T): Unit =
    subscribers.foreach(_.handle(event))

object ModelEventPublishers:
  val appointCreated = ModelEventPublisher[AppointCreated]()
  val appointUpdated = ModelEventPublisher[AppointUpdated]()
  val appointDeleted = ModelEventPublisher[AppointDeleted]()

  def publish(event: ModelEvent): Unit =
    event match {
      case e: AppointCreated => appointCreated.publish(e)
      case e: AppointUpdated => appointUpdated.publish(e)
      case e: AppointDeleted => appointDeleted.publish(e)
      case _ => ()
    }
