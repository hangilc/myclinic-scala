package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

object Implicits extends DateTime {

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

  given Encoder[InOperation] with
    def apply(op: InOperation): Json = Json.obj {
      "type" -> Json.fromString("in-operation")
    }
  given Encoder[RegularHoliday] with
    def apply(op: RegularHoliday): Json = Json.obj {
      "type" -> Json.fromString("regular-holiday")
    }
  given Encoder[AdHocHoliday] with
    def apply(op: AdHocHoliday): Json = Json.obj(
      "type" -> Json.fromString("ad-hoc-holiday"),
      "name" -> Json.fromString(op.name)
    )
  given Encoder[AdHocWorkday] with
    def apply(op: AdHocWorkday): Json = Json.obj(
      "type" -> Json.fromString("ad-hoc-workday"),
      "name" -> Json.fromString(op.name)
    )
  given Encoder[NationalHoliday] with
    def apply(op: NationalHoliday): Json = Json.obj(
      "type" -> Json.fromString("national-holiday"),
      "name" -> Json.fromString(op.name)
    )
  given Encoder[ClinicOperation] with
    def apply(cop: ClinicOperation): Json = cop match {
      case op @ InOperation()      => op.asJson
      case op @ RegularHoliday()   => op.asJson
      case op @ AdHocHoliday(_)    => op.asJson
      case op @ AdHocWorkday(_)    => op.asJson
      case op @ NationalHoliday(_) => op.asJson
    }
  given Decoder[ClinicOperation] with
    def apply(c: HCursor): Decoder.Result[ClinicOperation] =
      c.downField("type")
        .as[String]
        .flatMap(opType => {
          opType match {
            case "in-operation"    => Right(InOperation())
            case "regular-holiday" => Right(RegularHoliday())
            case "ad-hoc-holday" => {
              for name <- c.downField("name").as[String]
              yield AdHocHoliday(name)
            }
            case "ad-hoc-workday" => {
              for name <- c.downField("name").as[String]
              yield AdHocWorkday(name)
            }
            case "national-holday" => {
              for name <- c.downField("name").as[String]
              yield NationalHoliday(name)
            }
            case _ => Left(DecodingFailure("Invalid type field: ${opType}.", c.history))
          }
        })
}
