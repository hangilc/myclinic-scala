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
  val onStartCallbacks = new Callbacks[Unit]
  val onEndCallbacks = new Callbacks[Boolean]

  def append(task: T): Unit =
    queue = queue :+ task
    tryRun

  def scan(filter: T => Boolean): List[T] =
    val list = (current.map(List(_)).getOrElse(List.empty) ++ queue)
    list.filter(filter)

  private def tryRun: Unit =
    if current.isEmpty then
      queue match
        case Nil => ()
        case hd :: tl =>
          current = Some(hd)
          queue = tl
          onStartCallbacks.invoke(())
          hd.run().onComplete {
            case Success(_) =>
              current = None
              onEndCallbacks.invoke(true)
              tryRun
            case Failure(ex) =>
              current = None
              queue = Nil
              hd.onError(ex)
              onEndCallbacks.invoke(false)
          }
