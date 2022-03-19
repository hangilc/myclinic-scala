package dev.fujiwara.domq

trait DataSource[T]:
  def data: T
  def onUpdate(handler: T => Unit): Unit
  def onDelete(handler: T => Unit): Unit
  def isDeleted: Boolean

class LocalDataSource[T](init: T) extends DataSource[T]:
  private var cur: T = init
  private val onUpdatePublisher = LocalEventPublisher[T]
  private val onDeletePublisher = LocalEventPublisher[T]
  private var deletedFlag = false

  def data: T = cur
  def onUpdate(handler: T => Unit): Unit =
    onUpdatePublisher.subscribe(handler)
  def onDelete(handler: T => Unit): Unit =
    onDeletePublisher.subscribe(handler)

  def update(value: T): Unit =
    assert(!deletedFlag)
    cur = value
    onUpdatePublisher.publish(value)

  def delete(): Unit =
    assert(!deletedFlag)
    deletedFlag = true
    onDeletePublisher.publish(cur)

  def isDeleted: Boolean = deletedFlag

