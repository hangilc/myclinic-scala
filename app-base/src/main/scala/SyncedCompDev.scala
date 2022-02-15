package dev.myclinic.scala.web.appbase.dev

import dev.myclinic.scala.web.appbase.LocalEventPublisher

trait DataSource[T]:
  def data: T
  def onUpdate(handler: () => Unit): Unit
  def onDelete(handler: () => Unit): Unit

class LocalDataSouorce[T](init: T) extends DataSource[T]:
  private var cur: T = init
  private val onUpdatePublisher = LocalEventPublisher[Unit]
  private val onDeletePublisher = LocalEventPublisher[Unit]

  def data: T = cur
  def onUpdate(handler: () => Unit): Unit =
    onUpdatePublisher.subscribe(_ => handler())
  def onDelete(handler: () => Unit): Unit =
    onDeletePublisher.subscribe(_ => handler())

  def update(value: T): Unit =
    cur = value
    onUpdatePublisher.publish(())

  def delete(): Unit = onDeletePublisher.publish(())
