package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

object Implicits extends DateTime {

  implicit val appEventEncoder: Encoder[AppEvent] = deriveEncoder[AppEvent]
  implicit val appEventDecoder: Decoder[AppEvent] = deriveDecoder[AppEvent]

  implicit val appointTimeEncoder: Encoder[AppointTime] =
    deriveEncoder[AppointTime]
  implicit val appointTimeDecoder: Decoder[AppointTime] =
    deriveDecoder[AppointTime]

  implicit val appointEncoder: Encoder[Appoint] = deriveEncoder[Appoint]
  implicit val appointDecoder: Decoder[Appoint] = deriveDecoder[Appoint]

  implicit val patientEncoder: Encoder[Patient] = deriveEncoder[Patient]
  implicit val patientDecoder: Decoder[Patient] = deriveDecoder[Patient]

}
