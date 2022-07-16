package dev.fujiwara.domq

import scala.concurrent.Future
import dev.fujiwara.domq.TypeClasses.Dispose
import scala.util.Try
import scala.util.Success
import scala.util.Failure


class LocalEventPublisher[T]:
  private var subscribers: List[T => Unit] = List.empty
  def subscribe(handler: T => Unit): LocalEventUnsubscriber =
    subscribers = subscribers :+ handler
    LocalEventUnsubscriber(() => {
      subscribers = subscribers.filter(_ != handler)
    })
  def publish(t: T): Unit = subscribers.foreach(h => {
    Try(h(t)) match {
      case Success(_ ) => ()
      case Failure(ex) => System.err.println(ex.toString)
    }
  })

case class LocalEventUnsubscriber(proc: () => Unit):
  def unsubscribe(): Unit = proc()

object LocalEventUnsubscriber:
  given Dispose[LocalEventUnsubscriber] = _.unsubscribe()

class CachingEventPublisher[T](private var cache: T) extends LocalEventPublisher[T]:
  override def publish(t: T): Unit =
    cache = t
    super.publish(t)

  def currentValue: T = cache

object CachingEventPublisher:
  def apply[T](t: T): CachingEventPublisher[T] = new CachingEventPublisher(t)