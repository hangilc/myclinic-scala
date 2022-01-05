package dev.myclinic.scala.web.reception.scan

import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

trait WorkQueueTask:
  val run: () => Future[Unit]
  val onError: Throwable => Unit = ex => System.err.println(ex.getMessage)

class WorkQueue[T <: WorkQueueTask]:
  private var current: Option[T] = None
  private var queue: List[T] = List.empty
  val onStartCallbacks = new Callbacks[T]
  val onEndCallbacks = new Callbacks[(T, Boolean)]

  def append(task: T): Unit =
    queue = queue :+ task
    tryRun

  private def list: List[T] =
    current.map(List(_)).getOrElse(List.empty) ++ queue

  def find(pred: T => Boolean): Option[T] = list.find(pred)

  private def tryRun: Unit =
    if current.isEmpty then
      queue match
        case Nil => ()
        case hd :: tl =>
          current = Some(hd)
          queue = tl
          onStartCallbacks.invoke(hd)
          hd.run().onComplete {
            case Success(_) =>
              current = None
              onEndCallbacks.invoke(hd, true)
              tryRun
            case Failure(ex) =>
              current = None
              queue = Nil
              hd.onError(ex)
              onEndCallbacks.invoke(hd, false)
          }
