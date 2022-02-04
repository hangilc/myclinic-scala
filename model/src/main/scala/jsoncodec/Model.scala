package dev.myclinic.scala.model.jsoncodec

import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import dev.myclinic.scala.model._
import scala.util.Try
import java.time.LocalDate

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

  private[jsoncodec] given Encoder[HotlineBeep] = deriveEncoder[HotlineBeep]
  private[jsoncodec] given Decoder[HotlineBeep] = deriveDecoder[HotlineBeep]

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

  given Encoder[Shahokokuho] = deriveEncoder[Shahokokuho]
  given Decoder[Shahokokuho] = deriveDecoder[Shahokokuho]

  given Encoder[Roujin] = deriveEncoder[Roujin]
  given Decoder[Roujin] = deriveDecoder[Roujin]

  given Encoder[Koukikourei] = deriveEncoder[Koukikourei]
  given Decoder[Koukikourei] = deriveDecoder[Koukikourei]

  given Encoder[Kouhi] = deriveEncoder[Kouhi]
  given Decoder[Kouhi] = deriveDecoder[Kouhi]

  given Encoder[IyakuhinMaster] = deriveEncoder[IyakuhinMaster]
  given Decoder[IyakuhinMaster] = deriveDecoder[IyakuhinMaster]

  given Encoder[ShinryouMaster] = deriveEncoder[ShinryouMaster]
  given Decoder[ShinryouMaster] = deriveDecoder[ShinryouMaster]

  given Encoder[KizaiMaster] = deriveEncoder[KizaiMaster]
  given Decoder[KizaiMaster] = deriveDecoder[KizaiMaster]

  given Encoder[DrugEx] = deriveEncoder[DrugEx]
  given Decoder[DrugEx] = deriveDecoder[DrugEx]

  given Encoder[ShinryouEx] = deriveEncoder[ShinryouEx]
  given Decoder[ShinryouEx] = deriveDecoder[ShinryouEx]

  given Encoder[ConductEx] = deriveEncoder[ConductEx]
  given Decoder[ConductEx] = deriveDecoder[ConductEx]

  given Encoder[ConductDrugEx] = deriveEncoder[ConductDrugEx]
  given Decoder[ConductDrugEx] = deriveDecoder[ConductDrugEx]

  given Encoder[ConductShinryouEx] = deriveEncoder[ConductShinryouEx]
  given Decoder[ConductShinryouEx] = deriveDecoder[ConductShinryouEx]

  given Encoder[ConductKizaiEx] = deriveEncoder[ConductKizaiEx]
  given Decoder[ConductKizaiEx] = deriveDecoder[ConductKizaiEx]

  given optShahokokuhoEncoder: Encoder[Option[Shahokokuho]] =
    Encoder.encodeOption[Shahokokuho]
  given optShahokokuhoDecoder: Decoder[Option[Shahokokuho]] =
    Decoder.decodeOption[Shahokokuho]

  given optRoujinEncoder: Encoder[Option[Roujin]] = Encoder.encodeOption[Roujin]
  given optRoujinDecoder: Decoder[Option[Roujin]] = Decoder.decodeOption[Roujin]

  given optKoukikoureiEncoder: Encoder[Option[Koukikourei]] =
    Encoder.encodeOption[Koukikourei]
  given optKoukikoureiDecoder: Decoder[Option[Koukikourei]] =
    Decoder.decodeOption[Koukikourei]

  given optKouhiEncoder: Encoder[Option[Kouhi]] = Encoder.encodeOption[Kouhi]
  given optKouhiDecoder: Decoder[Option[Kouhi]] = Decoder.decodeOption[Kouhi]

  given optWqueueFullEncoder: Encoder[Option[(Int, Wqueue, Visit, Patient)]] = Encoder.encodeOption[(Int, Wqueue, Visit, Patient)]
  given optWqueueFullDecoder: Decoder[Option[(Int, Wqueue, Visit, Patient)]] = Decoder.decodeOption[(Int, Wqueue, Visit, Patient)]

  given Encoder[VisitEx] = deriveEncoder[VisitEx]
  given Decoder[VisitEx] = deriveDecoder[VisitEx]

  given Encoder[MeisaiSectionItem] = deriveEncoder[MeisaiSectionItem]
  given Decoder[MeisaiSectionItem] = deriveDecoder[MeisaiSectionItem]

  given Encoder[MeisaiSection] = new Encoder[MeisaiSection]{
    def apply(m: MeisaiSection): Json = Json.fromString(m.label)
  }
  given Decoder[MeisaiSection] = new Decoder[MeisaiSection]{
    def apply(c: HCursor): Decoder.Result[MeisaiSection] =
      for
        label <- c.as[String]
      yield {
        label match {
          case "初・再診料" => MeisaiSection.ShoshinSaisin
          case "医学管理等" => MeisaiSection.IgakuKanri
          case "在宅医療" => MeisaiSection.Zaitaku
          case "検査" => MeisaiSection.Kensa
          case "画像診断" => MeisaiSection.Gazou
          case "投薬" => MeisaiSection.Touyaku
          case "注射" => MeisaiSection.Chuusha
          case "処置" => MeisaiSection.Shochi
          case "その他" => MeisaiSection.Sonota
        }
      }
  }

  given Encoder[MeisaiSectionData] = deriveEncoder[MeisaiSectionData]
  given Decoder[MeisaiSectionData] = deriveDecoder[MeisaiSectionData]

  given Encoder[Meisai] = deriveEncoder[Meisai]
  given Decoder[Meisai] = deriveDecoder[Meisai]

  given Encoder[ValidUpto] = Encoder.encodeString.contramap(validUpto => validUpto.value match {
    case Some(date) => sqlDateFormatter.format(date)
    case None => "0000-00-00"
  })

  given Decoder[ValidUpto] = Decoder.decodeString.emapTry(str => Try{
    if str == "0000-00-00" then ValidUpto(None)
    else ValidUpto(Some(LocalDate.parse(str, sqlDateFormatter)))
  })

  given Encoder[ClinicInfo] = deriveEncoder[ClinicInfo]
  given Decoder[ClinicInfo] = deriveDecoder[ClinicInfo]

  given Encoder[ScannerDevice] = deriveEncoder[ScannerDevice]
  given Decoder[ScannerDevice] = deriveDecoder[ScannerDevice]

  given Encoder[FileInfo] = deriveEncoder[FileInfo]
  given Decoder[FileInfo] = deriveDecoder[FileInfo]


