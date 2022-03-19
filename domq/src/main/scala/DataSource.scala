package dev.fujiwara.domq

trait DataSource[T]:
  def data: T
  def onUpdate(handler: () => Unit): Unit
  def onDelete(handler: () => Unit): Unit
  def isDeleted: Boolean

class LocalDataSource[T](init: T) extends DataSource[T]:
  private var cur: T = init
  private val onUpdatePublisher = LocalEventPublisher[Unit]
  private val onDeletePublisher = LocalEventPublisher[Unit]
  private var deletedFlag = false

  def data: T = cur
  def onUpdate(handler: () => Unit): Unit =
    onUpdatePublisher.subscribe(_ => handler())
  def onDelete(handler: () => Unit): Unit =
    onDeletePublisher.subscribe(_ => handler())

  def update(value: T): Unit =
    assert(!deletedFlag)
    cur = value
    onUpdatePublisher.publish(())

  def delete(): Unit =
    deletedFlag = true
    onDeletePublisher.publish(())

  def isDeleted: Boolean = deletedFlag

