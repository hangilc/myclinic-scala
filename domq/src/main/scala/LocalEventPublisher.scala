package dev.fujiwara.domq

import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import dev.fujiwara.domq.TypeClasses.Dispose


class LocalEventPublisher[T]:
  private var subscribers: List[T => Future[Unit]] = List.empty
  def subscribe(handler: T => Unit): LocalEventUnsubscriber =
    val fh: T => Future[Unit] = t => Future.successful(handler(t))
    subscribeFuture(t => Future.successful(handler(t)))
  def subscribeFuture(handler: T => Future[Unit]): LocalEventUnsubscriber =
    subscribers = subscribers :+ handler
    LocalEventUnsubscriber(() => subscribers = subscribers.filter(_ != handler))
  def publishFuture(t: T): Future[Unit] = 
    Future.traverse(subscribers)(s => s(t)).map(_ => ())
  def publish(t: T): Unit = publishFuture(t)

case class LocalEventUnsubscriber(proc: () => Unit):
  def unsubscribe: Unit = proc()

object LocalEventUnsubscriber:
  given Dispose[LocalEventUnsubscriber] = _.unsubscribe

class CachingEventPublisher[T](private var cache: T) extends LocalEventPublisher[T]:
  override def publishFuture(t: T): Future[Unit] =
    cache = t
    super.publishFuture(t)

  def currentValue: T = cache

object CachingEventPublisher:
  def apply[T](t: T): CachingEventPublisher[T] = new CachingEventPublisher(t)