package dev.myclinic.scala.modeljson

import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._

object Implicits extends DateTime with ClinicOperationCodec {

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

  // given appointEncoder: Encoder[Appoint] = deriveEncoder[Appoint]
  // given appointDecoder: Decoder[Appoint] = deriveDecoder[Appoint]

  given Encoder[Appoint] = new Encoder[Appoint]:
    def apply(appoint: Appoint): Json =
      Json.obj(
        "appointId" -> Json.fromInt(appoint.appointId),
        "appointTimeId" -> Json.fromInt(appoint.appointTimeId),
        "patientName" -> Json.fromString(appoint.patientName),
        "patientId" -> Json.fromInt(appoint.patientId),
        "memo" -> encodeMemo(appoint.memo, appoint.tags)
      )
    def encodeMemo(memo: String, tags: List[String]): Json =
      val t = if tags.isEmpty then "" else "{{" + tags.mkString(",") + "}}"
      Json.fromString(t + memo)

  given Decoder[Appoint] = new Decoder[Appoint]:
    def apply(c: HCursor): Decoder.Result[Appoint] =
      for
        appointId <- c.downField("appointId").as[Int]
        appointTimeId <- c.downField("appointTimeId").as[Int]
        patientName <- c.downField("patientName").as[String]
        patientId <- c.downField("patientId").as[Int]
        memoWithTags <- c.downField("memo").as[String]
        (memo, tags) = decodeMemo(memoWithTags)
      yield Appoint(
        appointId,
        appointTimeId,
        patientName,
        patientId,
        memo,
        tags
      )
    def decodeMemo(
        s: String
    ): (String, List[String]) =
      val stop = s.indexOf("}}")
      if s.startsWith("{{") && stop >= 2 then
        val tags = s.substring(2, stop).split(",").toList
        val memo = s.substring(stop + 2)
        (memo, tags)
      else (s, List.empty)

  given patientEncoder: Encoder[Patient] = deriveEncoder[Patient]
  given patientDecoder: Decoder[Patient] = deriveDecoder[Patient]
  given Encoder[Option[Patient]] = Encoder.encodeOption[Patient]
  given Decoder[Option[Patient]] = Decoder.decodeOption[Patient]

}
