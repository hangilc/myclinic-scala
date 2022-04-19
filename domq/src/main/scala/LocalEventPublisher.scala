package dev.fujiwara.domq

class LocalEventPublisher[T]:
  private var subscribers: List[T => Unit] = List.empty
  // returns unsubscribe function
  def subscribe(handler: T => Unit): (() => Unit) =
    subscribers = subscribers :+ handler
    () => unsubscribe(handler)
  def unsubscribe(handler: T => Unit): Unit =
    subscribers = subscribers.filter(_ != handler)
  def publish(t: T): Unit = subscribers.foreach(_(t))

