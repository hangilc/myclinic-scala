package dev.fujiwara.domq

import scala.concurrent.Future
import scala.util.Try
import scala.concurrent.ExecutionContext
import scala.util.Success
import scala.util.Failure

class SingleTask[T](private var current: Option[TaskSet.Task[T]] = None)(using
    ExecutionContext
):
  def cancel(): Unit = current.foreach(_.cancel())
  def run(
      future: Future[T],
      onSuccess: T => Unit,
      onFailure: Throwable => Unit = ex => System.err.println(ex)
  ): Unit =
    cancel()
    current = Some(TaskSet.Task[T](future, onSuccess, onFailure))

object TaskSet:
  class Task[T](
      future: Future[T],
      onSuccess: T => Unit,
      onFailure: Throwable => Unit,
      var canceled: Boolean = false
  )(using
      ExecutionContext
  ):
    future.onComplete(result => {
      if !canceled then
        result match {
          case Success(t)  => onSuccess(t)
          case Failure(ex) => onFailure(ex)
        }
    })

    def cancel(): Unit =
      canceled = true
