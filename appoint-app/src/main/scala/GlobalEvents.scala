package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.web.appoint.sheet.AppointColumn

case class GlobalEventPublisher[T]():
  private var subscribers: Set[T => Unit] = Set.empty
  def subscribe(handler: T => Unit): Unit =
    subscribers = subscribers + handler

  def publish(event: T): Unit =
    subscribers.foreach(_(event))

object GlobalEvents:
  val AppointColumnChanged: GlobalEventPublisher[Seq[AppointColumn]] =
    GlobalEventPublisher()
