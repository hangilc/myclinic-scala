package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import io.circe._
import io.circe.generic.semiauto._
import JsonCodecs._

case class Appoint(
  date: LocalDate,
  time: LocalTime,
  patientName: String,
  patientId: Int,
  memo: String
)

object Appoint {
  implicit val appointEncoder: Encoder[Appoint] = deriveEncoder
  implicit val appointDecoder: Decoder[Appoint] = deriveDecoder

  def create(date: LocalDate, time: LocalTime): Appoint =
    Appoint(date, time, "", 0, "")
}