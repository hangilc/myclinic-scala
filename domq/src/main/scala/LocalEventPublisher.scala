package dev.fujiwara.domq

import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global


class LocalEventPublisher[T]:
  private var subscribers: List[T => Future[Unit]] = List.empty
  // returns unsubscribe function
  def subscribe(handler: T => Unit): (() => Unit) =
    val fh: T => Future[Unit] = t => Future.successful(handler(t))
    subscribeFuture(t => Future.successful(handler(t)))
  def subscribeFuture(handler: T => Future[Unit]): (() => Unit) =
    subscribers = subscribers :+ handler
    () => unsubscribe(handler)
  private def unsubscribe(handler: T => Future[Unit]): Unit =
    subscribers = subscribers.filter(_ != handler)
  def publish(t: T): Future[Unit] = 
    Future.traverse(subscribers)(s => s(t)).map(_ => ())

