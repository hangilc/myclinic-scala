package dev.myclinic.scala.web.appoint
import concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

object AppointHelper {
  def catchall[A](
      f: () => Future[A],
      cb: Either[Throwable, A] => Unit
  ): Unit = {
    try {
      f().onComplete {
        case Success(value) => cb(Right(value))
        case Failure(e)     => cb(Left(e))
      }
    } catch {
      case (e: Throwable) => cb(Left(e))
    }
  }
}
