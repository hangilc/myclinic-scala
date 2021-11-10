package dev.myclinic.scala.model

import java.time.LocalDateTime
import io.circe.*
import io.circe.syntax.*
import io.circe.parser.decode
import dev.myclinic.scala.model.jsoncodec.Implicits.given

case class AppEvent(
    appEventId: Int,
    createdAt: LocalDateTime,
    model: String,
    kind: String,
    data: String
)

sealed trait AppModelEvent

case class UnknownAppEvent(
    appEventId: Int,
    createdAt: LocalDateTime,
    model: String,
    kind: String,
    data: String
) extends AppModelEvent

case class AppointCreated(createdAt: LocalDateTime, created: Appoint)
    extends AppModelEvent
case class AppointUpdated(createdAt: LocalDateTime, updated: Appoint)
    extends AppModelEvent
case class AppointDeleted(createdAt: LocalDateTime, deleted: Appoint)
    extends AppModelEvent

case class AppointTimeCreated(createdAt: LocalDateTime, created: AppointTime)
    extends AppModelEvent
case class AppointTimeUpdated(createdAt: LocalDateTime, updated: AppointTime)
    extends AppModelEvent
case class AppointTimeDeleted(createdAt: LocalDateTime, deleted: AppointTime)
    extends AppModelEvent

object AppModelEvent:
  def from(event: AppEvent): AppModelEvent =
    val at = event.createdAt
    val data = event.data
    def as[T](using Decoder[T]): T = decode[T](data) match {
      case Right(t: T) => t
      case Left(ex)    => throw ex
    }
    (event.model, event.kind) match {
      case ("appoint", "created") => AppointCreated(at, as[Appoint])
      case ("appoint", "updated") => AppointCreated(at, as[Appoint])
      case ("appoint", "deleted") => AppointCreated(at, as[Appoint])
      case ("appoint-time", "created") =>
        AppointTimeCreated(at, as[AppointTime])
      case ("appoint-time", "updated") =>
        AppointTimeCreated(at, as[AppointTime])
      case ("appoint-time", "deleted") =>
        AppointTimeCreated(at, as[AppointTime])
      case _ =>
        UnknownAppEvent(
          event.appEventId,
          event.createdAt,
          event.model,
          event.kind,
          event.data
        )
    }
