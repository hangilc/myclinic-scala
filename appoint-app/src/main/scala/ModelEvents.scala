package dev.myclinic.scala.event

import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.modeljson.Implicits.{given}
import scala.util.Success
import scala.util.Failure

object ModelEvents {

  sealed trait ModelEvent
  case class AppointCreated(created: Appoint) extends ModelEvent
  case class AppointUpdated(updated: Appoint) extends ModelEvent
  case class AppointDeleted(deleted: Appoint) extends ModelEvent
  case class Unknown(orig: AppEvent) extends ModelEvent

  def convert(appEvent: AppEvent): ModelEvent = appEvent match
    case AppEvent(_, _, _, "appoint", kind, encodedData) => {
      val data = decode[Appoint](encodedData) match
        case Right(value) => value
        case Left(ex) => throw ex
      kind match
        case "created" => AppointCreated(data)
        case "updated" => AppointUpdated(data)
        case "deleted" => AppointDeleted(data)
    }
    case _ => Unknown(appEvent)
}
