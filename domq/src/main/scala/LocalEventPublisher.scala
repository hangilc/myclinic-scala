package dev.fujiwara.domq

import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import dev.fujiwara.domq.TypeClasses.Dispose


class LocalEventPublisher[T]:
  private var subscribers: List[T => Unit] = List.empty
  def subscribe(handler: T => Unit): LocalEventUnsubscriber =
    subscribers = subscribers :+ handler
    LocalEventUnsubscriber(() => {
      subscribers = subscribers.filter(_ != handler)
    })
  def publish(t: T): Unit = 
    subscribers.foreach(_(t))

case class LocalEventUnsubscriber(proc: () => Unit):
  def unsubscribe: Unit = proc()

object LocalEventUnsubscriber:
  given Dispose[LocalEventUnsubscriber] = _.unsubscribe
