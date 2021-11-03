package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

import dev.myclinic.scala.clinicop.*
import java.time.LocalDate

trait ClinicOperationCodec:
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
            case "ad-hoc-holiday" => {
              for name <- c.downField("name").as[String]
              yield AdHocHoliday(name)
            }
            case "ad-hoc-workday" => {
              for name <- c.downField("name").as[String]
              yield AdHocWorkday(name)
            }
            case "national-holiday" => {
              for name <- c.downField("name").as[String]
              yield NationalHoliday(name)
            }
            case _ => Left(DecodingFailure(s"Invalid type field: ${opType}.", c.history))
          }
        })

