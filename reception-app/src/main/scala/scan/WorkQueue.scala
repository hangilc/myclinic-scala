package dev.myclinic.scala.web.reception.scan

import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

case class Task[T](
    tag: T,
    run: () => Future[Unit],
    onError: Throwable => Unit = ex => System.err.println(ex.getMessage)
)

class WorkQueue[T]:
  private var current: Option[Task[T]] = None
  private var queue: List[Task[T]] = List.empty

  def append(task: Task[T]): Unit =
    queue = queue :+ task
    tryRun

  def scan(filter: Task[T] => Boolean): List[Task[T]] =
    (current.map(List(_)).getOrElse(List.empty) ++ queue).filter(filter)

  private def tryRun: Unit =
    if current.isEmpty then
      queue match 
        case Nil => ()
        case hd :: tl =>
          current = Some(hd)
          queue = tl
          hd.run().onComplete { 
            case Success(_) =>
              current = None
              tryRun
            case Failure(ex) =>
              current = None
              queue = Nil
              hd.onError(ex)
          }
