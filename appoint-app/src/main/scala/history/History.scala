package dev.myclinic.scala.web.appoint.history

import dev.myclinic.scala.model.{AppEvent, Appoint, AppointTime}
import scala.concurrent.Future
import dev.myclinic.scala.web.appoint.Misc
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.webclient.Api
import java.time.LocalDateTime
import dev.myclinic.scala.event.ModelEvents
import cats._
import cats.syntax.all._

trait History:
  def description: String
  def resume: Option[() => Future[Either[String, Unit]]]
  def createdAt: LocalDateTime

object History:
  def fromAppEvents(appEvents: List[AppEvent]): Future[List[History]] =
    appEvents
      .map(e => appEventToHistory(e))
      .collect(opt => opt match {
        case Some(f) => f
      })
      .sequence

  def appEventToHistory(appEvent: AppEvent): Option[Future[History]] =
    val modelEvent = ModelEvents.convert(appEvent)
    val createdAt: LocalDateTime = appEvent.createdAt
    modelEvent match {
      case m: ModelEvents.AppointCreated => {
        Some(Api
          .getAppointTime(m.created.appointTimeId)
          .map(appointTime =>
            AppointCreated(m.created, appointTime, createdAt)
          ))
      }
      case m: ModelEvents.AppointUpdated => {
        Some(Api
          .getAppointTime(m.updated.appointTimeId)
          .map(appointTime =>
            AppointUpdated(m.updated, appointTime, createdAt)
          ))
      }
      case m: ModelEvents.AppointDeleted => {
        Some(Api
          .getAppointTime(m.deleted.appointTimeId)
          .map(appointTime =>
            AppointDeleted(m.deleted, appointTime, createdAt)
          ))
      }
      case _ => None
    }

case class AppointCreated(
    appoint: Appoint,
    appointTime: AppointTime,
    val createdAt: LocalDateTime
) extends History:
  def description: String = {
    val date = Misc.formatAppointDate(appointTime.date)
    val time = Misc.formatAppointTime(appointTime.fromTime)
    s"予約作成：${appoint.patientName} ${date} ${time}"
  }
  def resume: Option[() => Future[Either[String, Unit]]] = None

case class AppointUpdated(
    appoint: Appoint,
    appointTime: AppointTime,
    val createdAt: LocalDateTime
) extends History:
  def description: String = {
    val date = Misc.formatAppointDate(appointTime.date)
    val time = Misc.formatAppointTime(appointTime.fromTime)
    s"予約の修正：${appoint.patientName} ${date} ${time}"
  }
  def resume: Option[() => Future[Either[String, Unit]]] = None

case class AppointDeleted(
    appoint: Appoint,
    appointTime: AppointTime,
    val createdAt: LocalDateTime
) extends History:
  def description: String = {
    val date = Misc.formatAppointDate(appointTime.date)
    val time = Misc.formatAppointTime(appointTime.fromTime)
    s"予約キャンセル：${appoint.patientName} ${date} ${time}"
  }
  def resume: Option[() => Future[Either[String, Unit]]] = None
