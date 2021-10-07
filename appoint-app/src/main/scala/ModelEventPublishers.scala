package dev.myclinic.scala.event

import dev.myclinic.scala.event.ModelEvents.*

case class ModelEventSubscriber[T <: ModelEvent](handler: T => Unit)

case class ModelEventPublisher[T <: ModelEvent](
    var subscribers: Set[ModelEventSubscriber[T]] = Set.empty[ModelEventSubscriber[T]]
):
  def subscribe(handler: T => Unit): Unit =
    subscribers = subscribers + ModelEventSubscriber(handler)
  def publish(event: T): Unit =
    subscribers.foreach(sub => {
      try {
        sub.handler(event)
      } catch {
        case e: Throwable => System.err.println(e.toString())
      }
    })

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
