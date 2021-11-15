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

case class HotlineCreated(
    createdAt: LocalDateTime,
    appEventId: Int,
    created: Hotline
) extends AppModelEvent
case class HotlineBeep(createdAt: LocalDateTime, recipient: String)
    extends AppModelEvent

case class VisitCreated(createdAt: LocalDateTime, created: Visit)
    extends AppModelEvent
case class VisitUpdated(createdAt: LocalDateTime, updated: Visit)
    extends AppModelEvent
case class VisitDeleted(createdAt: LocalDateTime, deleted: Visit)
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
      case ("appoint", "updated") => AppointUpdated(at, as[Appoint])
      case ("appoint", "deleted") => AppointDeleted(at, as[Appoint])
      case ("appoint-time", "created") =>
        AppointTimeCreated(at, as[AppointTime])
      case ("appoint-time", "updated") =>
        AppointTimeUpdated(at, as[AppointTime])
      case ("appoint-time", "deleted") =>
        AppointTimeDeleted(at, as[AppointTime])
      case ("hotline", "created") =>
        HotlineCreated(at, event.appEventId, as[Hotline])
      case ("hotline", "beep") => HotlineBeep(at, as[String])
      case ("visit", "created") => VisitCreated(at, as[Visit])
      case ("visit", "updated") => VisitUpdated(at, as[Visit])
      case ("visit", "deleted") => VisitDeleted(at, as[Visit])
      case _ =>
        UnknownAppEvent(
          event.appEventId,
          event.createdAt,
          event.model,
          event.kind,
          event.data
        )
    }
