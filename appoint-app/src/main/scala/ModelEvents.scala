package dev.myclinic.scala.event

import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import scala.util.Success
import scala.util.Failure
import cats.Foldable

object ModelEvents:

  sealed trait ModelEvent
  case class Unknown(orig: AppEvent) extends ModelEvent
  case class AppointCreated(created: Appoint) extends ModelEvent
  case class AppointUpdated(updated: Appoint) extends ModelEvent
  case class AppointDeleted(deleted: Appoint) extends ModelEvent
  case class AppointTimeCreated(created: AppointTime) extends ModelEvent
  case class AppointTimeUpdated(updated: AppointTime) extends ModelEvent
  case class AppointTimeDeleted(deleted: AppointTime) extends ModelEvent

  def convert(appEvent: AppEvent): ModelEvent =
    appEvent.model match {
      case "appoint" =>
        val data: Appoint = decodeData(appEvent)
        appEvent.kind match
          case "created" => AppointCreated(data)
          case "updated" => AppointUpdated(data)
          case "deleted" => AppointDeleted(data)
      case "appoint-time" =>
        val data: AppointTime = decodeData(appEvent)
        appEvent.kind match
          case "created" => AppointTimeCreated(data)
          case "updated" => AppointTimeUpdated(data)
          case "deleted" => AppointTimeDeleted(data)
      case _ => Unknown(appEvent)
    }

  def decodeData[T](appEvent: AppEvent)(using Decoder[T]): T =
    decode[T](appEvent.data) match
      case Right(value) => value
      case Left(ex) => throw ex
