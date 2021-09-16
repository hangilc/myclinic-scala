package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

object Implicits extends DateTime {

  implicit val appointEncoder: Encoder[Appoint] = deriveEncoder[Appoint]
  implicit val appointDecoder: Decoder[Appoint] = deriveDecoder[Appoint]
  implicit val appEventEncoder: Encoder[AppEvent] = deriveEncoder[AppEvent]
  implicit val appEventDecoder: Decoder[AppEvent] = deriveDecoder[AppEvent]

}