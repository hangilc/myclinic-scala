package dev.myclinic.scala.web.appbase

class LocalEventPublisher[T]:
  private var subscribers: List[T => Unit] = List.empty
  def subscribe(handler: T => Unit): Unit =
    subscribers = subscribers :+ handler
  def publish(t: T): Unit = subscribers.foreach(_(t))

