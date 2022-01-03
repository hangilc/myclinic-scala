package dev.myclinic.scala.web.reception.scan

class Variable[T](private var store: T):
  def value: T = store
  private var callbacks: List[() => Unit] = List.empty
  def addCallback(f: () => Unit): Unit = callbacks = callbacks :+ f
  def removeCallback(f: () => Unit): Unit = callbacks = callbacks.filterNot(_ == f)
  def invoke(): Unit = callbacks.foreach(_())
  def set(newValue: T): Unit = store = newValue
  def update(newValue: T): Unit = 
    set(newValue)
    invoke()

object Variable:
  def apply[T](init: T): Variable[T] = new Variable(init)