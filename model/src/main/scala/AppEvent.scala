package dev.myclinic.scala.model

import java.time.LocalDateTime
import io.circe.*
import io.circe.syntax.*
import io.circe.parser.decode
import io.circe.generic.semiauto._
import dev.myclinic.scala.model.jsoncodec.Implicits.given

case class AppEvent(
    appEventId: Int,
    createdAt: LocalDateTime,
    model: String,
    kind: String,
    data: String
)

case class AppModelEvent(
    appEventId: Int,
    createdAt: LocalDateTime,
    model: String,
    kind: String,
    data: Any
):
  def dataAs[T]: T = data.asInstanceOf[T]

object AppModelEvent:
  val createdSymbol = "created"
  val updatedSymbol = "updated"
  val deletedSymbol = "deleted"
  def from(event: AppEvent): AppModelEvent =
    def as[T](using Decoder[T]): AppModelEvent = decode[T](event.data) match {
      case Right(t: T) => AppModelEvent(
          event.appEventId,
          event.createdAt,
          event.model,
          event.kind,
          t
      )
      case Left(ex)    => throw ex
    }
    event.model match {
      case Appoint.modelSymbol => as[Appoint]
      case AppointTime.modelSymbol => as[AppointTime]
      case Visit.modelSymbol => as[Visit]
      case Wqueue.modelSymbol => as[Wqueue]
      case Charge.modelSymbol => as[Charge]
      case Shahokokuho.modelSymbol => as[Shahokokuho]
      case Koukikourei.modelSymbol => as[Koukikourei]
      case Roujin.modelSymbol => as[Roujin]
      case Kouhi.modelSymbol => as[Kouhi]
      case Payment.modelSymbol => as[Payment]
      case Patient.modelSymbol => as[Patient]
      case Hotline.modelSymbol => as[Hotline]
      case _ =>
        AppModelEvent(
          event.appEventId,
          event.createdAt,
          event.model,
          event.kind,
          event.data
        )
    }
