package dev.myclinic.scala.event

import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.modeljson.Implicits.{given}
import scala.util.Success
import scala.util.Failure
import cats.Foldable

private def getMaxEventIdOne[F[_]: Foldable](as: F[Evented]): Int =
  summon[Foldable[F]].foldLeft(as, 0)((acc, ele) => acc.max(ele.eventId))

def getMaxEventId[F[_]: Foldable](las: F[Evented]*): Int =
  las.foldLeft(0)((acc, ele) => acc.max(getMaxEventIdOne(ele)))

object ModelEvents {

  sealed trait ModelEvent(val eventId: Int)
  case class AppointCreated(eventIdArg: Int, created: Appoint) extends ModelEvent(eventIdArg)
  case class AppointUpdated(eventIdArg: Int, updated: Appoint) extends ModelEvent(eventIdArg)
  case class AppointDeleted(eventIdArg: Int, deleted: Appoint) extends ModelEvent(eventIdArg)
  case class Unknown(eventIdArg: Int, orig: AppEvent) extends ModelEvent(eventIdArg)

  def convert(appEvent: AppEvent): ModelEvent = 
    appEvent.model match {
      case "appoint" => 
        val data = decode[Appoint](appEvent.data) match
          case Right(value) => value
          case Left(ex) => throw ex
        appEvent.kind match
          case "created" => AppointCreated(appEvent.eventId, data)
          case "updated" => AppointUpdated(appEvent.eventId, data)
          case "deleted" => AppointDeleted(appEvent.eventId, data)
      case _ => Unknown(appEvent.eventId, appEvent)
    }
}
