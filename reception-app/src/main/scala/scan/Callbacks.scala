package dev.myclinic.scala.web.reception.scan

class Callbacks:
  private var callbacks: List[() => Unit] = List.empty
  def add(f: () => Unit): Unit = callbacks = callbacks :+ f
  def remove(f: () => Unit): Unit = callbacks = callbacks.filterNot(_ == f)
  def invoke(): Unit = callbacks.foreach(_())
