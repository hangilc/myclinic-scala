package dev.myclinic.scala.web.reception.scan.variable

import org.scalajs.dom.{HTMLSelectElement}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import dev.fujiwara.domq.ElementQ.{*, given}

trait Variable[T]:
  def get: T 
  def set(value: T): Unit

class CachedVariable[T](private var cache: T) extends Variable[T]:
  override def get: T = cache
  override def set(value: T): Unit = cache = value

class SelectVariable(select: HTMLSelectElement) extends Variable[String]:
  def get: String = select.getValue
  def set(value: String): Unit = select.setValue(value)

trait Callbacks[T] extends Variable[T]:
  private var callbacks: List[T => Unit] = List.empty
  def addCallback(cb: T => Unit): Unit =
    callbacks = callbacks :+ cb
  def invokeCallbacks(value: T): Unit = 
    callbacks.foreach(cb => cb(value))
  override def set(value: T): Unit =
    //super.set(value)
    invokeCallbacks(value)

trait FutureCallbacks[T]:
  private var callbacks: List[T => Future[Unit]] = List.empty
  def addCallback(cb: T => Future[Unit]): Unit =
    callbacks = callbacks :+ cb
  private def invoke(value: T, cbs: List[T => Future[Unit]]): Future[Unit] =
    cbs match {
      case Nil => Future.successful(())
      case h :: t => h(value).flatMap(_ => invoke(value, t))
    }
  def invokeCallbacks(value: T): Future[Unit] = invoke(value, callbacks)

class CachedVariableWithCallbacks[T](init: T) extends CachedVariable[T](init) with Callbacks[T]

    
