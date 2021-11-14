package dev.myclinic.scala.model.jsoncodec

import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

trait Model extends DateTime with WaitStateCodec:

  given Encoder[Sex] = new Encoder[Sex]:
    def apply(sex: Sex): Json =
      sex match {
        case Sex.Male   => Json.fromString("M")
        case Sex.Female => Json.fromString("F")
      }
  given Decoder[Sex] = Decoder.decodeString.emap(s =>
    s match {
      case "M" => Right(Sex.Male)
      case "F" => Right(Sex.Female)
      case _   => Left(s"Cannot decode sex (${s}).")
    }
  )

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
  given Encoder[Option[Patient]] = Encoder.encodeOption[Patient]
  given Decoder[Option[Patient]] = Decoder.decodeOption[Patient]

  given Encoder[Hotline] = deriveEncoder[Hotline]
  given Decoder[Hotline] = deriveDecoder[Hotline]

  given Encoder[Wqueue] = deriveEncoder[Wqueue]
  given Decoder[Wqueue] = deriveDecoder[Wqueue]


