package dev.myclinic.scala.model

import java.time.LocalDateTime

object Events {

  trait JsonEncoder {
    def encode[T](value: T): String
  }

  trait JsonDecoder {
    def decode[T](json: String): T
  }

  case class FromTo[T](from: T, to: T)

  def createAppointCreatedEvent(
      created: Appoint
  )(implicit encoder: JsonEncoder): AppEvent = {
    AppEvent(
      0,
      LocalDateTime.now(),
      "appoint",
      "created",
      encoder.encode(created)
    )
  }

  def createAppointUpdatedEvent(
      from: Appoint,
      to: Appoint
  )(implicit encoder: JsonEncoder): AppEvent = {
    AppEvent(
      0,
      LocalDateTime.now(),
      "appoint",
      "updated",
      encoder.encode(FromTo(from, to))
    )
  }

  def createAppointDeletedEvent(
      deleted: Appoint
  )(implicit encoder: JsonEncoder): AppEvent = {
    AppEvent(
      0,
      LocalDateTime.now(),
      "appoint",
      "deleted",
      encoder.encode(deleted)
    )
  }

}
