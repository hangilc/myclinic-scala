package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

object Implicits extends DateTime {

  given Encoder[Appoint] = deriveEncoder[Appoint]
  given Decoder[Appoint] = deriveDecoder[Appoint]
  given Encoder[AppEvent] = deriveEncoder[AppEvent]
  given Decoder[AppEvent] = deriveDecoder[AppEvent]

}