package dev.myclinic.scala.web.appoint

import scala.concurrent.Future

object AppointHelper {
  def catchall[A](f: => Future[A]): Future[A] = {
    try {
      f
    } catch {
      case (e: Throwable) => Future.failed(e)
    }
  }
}
