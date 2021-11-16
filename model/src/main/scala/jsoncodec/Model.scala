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

  given Encoder[Visit] = deriveEncoder[Visit]
  given Decoder[Visit] = deriveDecoder[Visit]

  given Encoder[Text] = deriveEncoder[Text]
  given Decoder[Text] = deriveDecoder[Text]

  given Encoder[Drug] = deriveEncoder[Drug]
  given Decoder[Drug] = deriveDecoder[Drug]

  given Encoder[Shinryou] = deriveEncoder[Shinryou]
  given Decoder[Shinryou] = deriveDecoder[Shinryou]

  given Encoder[Conduct] = deriveEncoder[Conduct]
  given Decoder[Conduct] = deriveDecoder[Conduct]

  given Encoder[ConductDrug] = deriveEncoder[ConductDrug]
  given Decoder[ConductDrug] = deriveDecoder[ConductDrug]

  given Encoder[ConductShinryou] = deriveEncoder[ConductShinryou]
  given Decoder[ConductShinryou] = deriveDecoder[ConductShinryou]

  given Encoder[ConductKizai] = deriveEncoder[ConductKizai]
  given Decoder[ConductKizai] = deriveDecoder[ConductKizai]

  given Encoder[Charge] = deriveEncoder[Charge]
  given Decoder[Charge] = deriveDecoder[Charge]

  given Encoder[Payment] = deriveEncoder[Payment]
  given Decoder[Payment] = deriveDecoder[Payment]

  given Encoder[IyakuhinMaster] = deriveEncoder[IyakuhinMaster]
  given Decoder[IyakuhinMaster] = deriveDecoder[IyakuhinMaster]

  given Encoder[ShinryouMaster] = deriveEncoder[ShinryouMaster]
  given Decoder[ShinryouMaster] = deriveDecoder[ShinryouMaster]

  given Encoder[KizaiMaster] = deriveEncoder[KizaiMaster]
  given Decoder[KizaiMaster] = deriveDecoder[KizaiMaster]

  


