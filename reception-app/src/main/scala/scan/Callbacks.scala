package dev.myclinic.scala.web.reception.scan

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Callbacks:
  private var callbacks: List[() => Unit] = List.empty
  def add(f: () => Unit): Unit = callbacks = callbacks :+ f
  def remove(f: () => Unit): Unit = callbacks = callbacks.filterNot(_ == f)
  def invoke(): Unit = callbacks.foreach(_())

class FutureCallbacks:
  private var callbacks: List[() => Future[Unit]] = List.empty
  def add(f: () => Future[Unit]): Unit = callbacks = callbacks :+ f
  def remove(f: () => Future[Unit]): Unit = callbacks = callbacks.filterNot(_ == f)
  def invoke(): Future[Unit] = invokeIter(callbacks)
  private def invokeIter(cbs: List[() => Future[Unit]]): Future[Unit] =
    cbs match {
      case Nil => Future.successful(())
      case h :: t => h().flatMap(_ => invokeIter(t))
    }
