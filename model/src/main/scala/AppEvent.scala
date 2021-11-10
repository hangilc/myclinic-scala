package dev.myclinic.scala.model

import java.time.LocalDateTime

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

case class AppointCreated(
    val appEventId: Int,
    val createdAt: LocalDateTime,
    data: Appoint
) extends AppModelEvent

case class AppointUpdated(
    val appEventId: Int,
    val createdAt: LocalDateTime,
    data: Appoint
) extends AppModelEvent

case class AppointDeleted(
    val appEventId: Int,
    val createdAt: LocalDateTime,
    data: Appoint
) extends AppModelEvent

object AppModelEvent:
  def from(event: AppEvent): AppModelEvent =
    event match {
      case _ => UnknownAppEvent(
        event.appEventId,
        event.createdAt,
        event.model,
        event.kind,
        event.data
      )
    }