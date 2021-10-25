package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

object Implicits extends DateTime {

  given appEventEncoder: Encoder[AppEvent] = deriveEncoder[AppEvent]
  given appEventDecoder: Decoder[AppEvent] = deriveDecoder[AppEvent]

  given appointTimeEncoder: Encoder[AppointTime] =
    deriveEncoder[AppointTime]
  given appointTimeDecoder: Decoder[AppointTime] =
    deriveDecoder[AppointTime]

  given appointEncoder: Encoder[Appoint] = deriveEncoder[Appoint]
  given appointDecoder: Decoder[Appoint] = deriveDecoder[Appoint]

  given patientEncoder: Encoder[Patient] = deriveEncoder[Patient]
  given patientDecoder: Decoder[Patient] = deriveDecoder[Patient]

}
