package dev.fujiwara.domq

class LocalEventPublisher[T]:
  private var subscribers: List[T => Unit] = List.empty
  def subscribe(handler: T => Unit): Unit =
    subscribers = subscribers :+ handler
  def subscribe(publisher: LocalEventPublisher[T]): Unit =
    subscribe(t => publisher.publish(t))
  def unsubscribe(handler: T => Unit): Unit =
    subscribers = subscribers.filter(_ != handler)
  def publish(t: T): Unit = subscribers.foreach(_(t))

