package dev.myclinic.scala.web.appoint.history

import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.given
import scala.concurrent.Future
import dev.myclinic.scala.web.appoint.Misc
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.webclient.Api
import java.time.LocalDateTime
import cats._
import cats.syntax.all._
import dev.fujiwara.kanjidate.KanjiDate

trait History:
  def description: String
  def resume: Option[() => Future[Either[String, Unit]]]
  def createdAt: LocalDateTime

object History:
  def fromAppEvents(appEvents: List[AppEvent]): Future[List[History]] =
    appEvents
      .map(e => appEventToHistory(e))
      .collect(opt =>
        opt match {
          case Some(f) => f
        }
      )
      .sequence

  def appEventToHistory(appEvent: AppEvent): Option[Future[History]] =
    val modelEvent = AppModelEvent.from(appEvent)
    val createdAt: LocalDateTime = appEvent.createdAt
    val M = Appoint.modelSymbol
    (modelEvent.model, modelEvent.kind) match {
      case (M, AppModelEvent.createdSymbol) => {
        val m: Appoint = modelEvent.dataAs[Appoint]
        Some(
          Api
            .getAppointTime(m.appointTimeId)
            .map(appointTime =>
              AppointCreatedHistory(m, appointTime, createdAt)
            )
        )
      }
      case (M, AppModelEvent.updatedSymbol) => {
        val m: Appoint = modelEvent.dataAs[Appoint]
        Some(
          Api
            .getAppointTime(m.appointTimeId)
            .map(appointTime =>
              AppointUpdatedHistory(m, appointTime, createdAt)
            )
        )
      }
      case (M, AppModelEvent.deletedSymbol) => {
        val m: Appoint = modelEvent.dataAs[Appoint]
        Some(
          Api
            .getAppointTime(m.appointTimeId)
            .map(appointTime =>
              AppointDeletedHistory(m, appointTime, createdAt)
            )
        )
      }
      case _ => None
    }

case class AppointCreatedHistory(
    appoint: Appoint,
    appointTime: AppointTime,
    createdAt: LocalDateTime
) extends History:
  def description: String = {
    "【作成】" + Renderer.renderAppoint(appoint, appointTime, createdAt)
  }
  def resume: Option[() => Future[Either[String, Unit]]] = None

case class AppointUpdatedHistory(
    appoint: Appoint,
    appointTime: AppointTime,
    val createdAt: LocalDateTime
) extends History:
  def description: String = {
    "【変更】" + Renderer.renderAppoint(appoint, appointTime, createdAt)
  }
  def resume: Option[() => Future[Either[String, Unit]]] = None

case class AppointDeletedHistory(
    appoint: Appoint,
    appointTime: AppointTime,
    val createdAt: LocalDateTime
) extends History:
  def description: String = {
    "【削除】" + Renderer.renderAppoint(appoint, appointTime, createdAt)
  }
  def resume: Option[() => Future[Either[String, Unit]]] = None

object Renderer:
  def renderAppoint(a: Appoint, t: AppointTime, stamp: LocalDateTime): String =
    val dt: String = Misc.formatAppointDateTime(t)
    val patientIdRep = if a.patientId == 0 then "" else a.patientId.toString
    val tagsRep = a.tags.mkString("、")
    val createdAt =
      KanjiDate.dateToKanji(stamp.toLocalDate) + KanjiDate.timeToKanji(
        stamp.toLocalTime
      )
    s"${dt}:${a.patientName}:${patientIdRep}:${a.memoString}:${tagsRep}（${createdAt}）"
