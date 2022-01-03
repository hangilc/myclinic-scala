package dev.myclinic.scala.web.reception.scan

import scala.concurrent.Future

class Variable[T](private var store: T):
  def value: T = store
  val callbacks = new Callbacks()
  def set(newValue: T): Unit = store = newValue
  def update(newValue: T): Unit = 
    set(newValue)
    callbacks.invoke()

class FutureVariable[T](private var store: T):
  def value: T = store
  val callbacks = new FutureCallbacks()
  def set(newValue: T): Unit = store = newValue
  def update(newValue: T): Future[Unit] = 
    set(newValue)
    callbacks.invoke()
