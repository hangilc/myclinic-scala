package dev.myclinic.scala.web.reception.scan

import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._


class Callbacks[T]:
  type F = T => Unit
  private var callbacks: List[F] = List.empty
  def add(f: F): Unit = callbacks = callbacks :+ f
  def remove(f: F): Unit = callbacks = callbacks.filterNot(_ == f)
  def invoke(t: T): Unit = callbacks.foreach(_(t))

class FutureCallbacks[T]:
  type F = T => Future[Unit]
  private var callbacks: List[F] = List.empty
  def add(f: F): Unit = callbacks = callbacks :+ f
  def remove(f: F): Unit = callbacks = callbacks.filterNot(_ == f)
  def invoke(t: T): Future[Unit] = invokeIter(t, callbacks)
  private def invokeIter(t: T, cbs: List[F]): Future[Unit] =
    cbs match {
      case Nil => Future.successful(())
      case hd :: tl => hd(t).flatMap(_ => invokeIter(t, tl))
    }
