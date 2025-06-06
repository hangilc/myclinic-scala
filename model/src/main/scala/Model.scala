package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import scala.math.Ordered.orderingToOrdered
import scala.language.implicitConversions
import io.circe.*
import io.circe.syntax.*
import io.circe.parser.decode
import io.circe.generic.semiauto._
import scala.concurrent.duration.FiniteDuration
import dev.fujiwara.kanjidate.DateUtil
import dev.myclinic.scala.util.HokenRep

case class ValidUpto(value: Option[LocalDate]):
  def isEqualOrAfter(d: LocalDate): Boolean =
    value.fold(true)(v => d <= v)

object ValidUpto:
  given Conversion[ValidUpto, LocalDate] with
    def apply(src: ValidUpto): LocalDate =
      src.value match {
        case Some(v) => v
        case None    => LocalDate.MAX
      }

case class AppointTime(
    appointTimeId: Int,
    date: LocalDate,
    fromTime: LocalTime,
    untilTime: LocalTime,
    kind: String,
    capacity: Int
):
  def isAdjacentTo(other: AppointTime): Boolean =
    date == other.date && untilTime == other.fromTime

  def overlapsWith(other: AppointTime): Boolean =
    date == other.date &&
      untilTime > other.fromTime &&
      fromTime < other.untilTime

object AppointTime:
  def overlaps(ats: List[AppointTime]): Boolean =
    val byDate: Map[LocalDate, List[AppointTime]] = ats.groupBy(at => at.date)
    byDate.values
      .map(ls => ls.sortBy(at => at.fromTime))
      .map(ls => ls.map(at => (at.fromTime, at.untilTime)))
      .map(timeIntervalOverlaps(_))
      .reduce(_ || _)

  def timeIntervalOverlaps(
      sortedIntervals: List[(LocalTime, LocalTime)]
  ): Boolean =
    sortedIntervals match {
      case Nil => false
      case l @ _ :: t =>
        (l.zip(t)
          .map { case ((_, a), (b, _)) =>
            a.isAfter(b)
          })
          .exists(identity)
    }

  def isAdjacentRun(as: List[AppointTime]): Boolean =
    if as.size < 2 then true
    else as.sliding(2).forall(e => e(0).isAdjacentTo(e(1)))

  def extractAdjacentRun(
      as: List[AppointTime]
  ): (List[AppointTime], List[AppointTime]) =
    extractAdjacentRunEmbedded(as, identity)

  def extractAdjacentRunEmbedded[T](
      ts: List[T],
      acc: T => AppointTime
  ): (List[T], List[T]) =
    def extendOne(
        reversedRun: List[T],
        rest: List[T]
    ): (List[T], List[T]) =
      (reversedRun, rest) match {
        case (h :: t, rh :: rt) if acc(h).isAdjacentTo(acc(rh)) =>
          (rh :: reversedRun, rt)
        case _ => (reversedRun, rest)
      }
    def loop(reversedRun: List[T], rest: List[T]): (List[T], List[T]) =
      val (rr, r) = extendOne(reversedRun, rest)
      if rr.head == reversedRun.head then (rr.reverse, r)
      else loop(rr, r)
    ts match {
      case h :: t => loop(List(h), t)
      case _      => (List.empty, ts)
    }

  given Ordering[AppointTime] = Ordering.by(a => (a.date, a.fromTime))
  val modelSymbol: String = "appoint-time"
  given ModelSymbol[AppointTime] with
    def getSymbol: String = modelSymbol
  given DataId[AppointTime] = _.appointTimeId

case class Appoint(
    appointId: Int,
    appointTimeId: Int,
    patientName: String,
    patientId: Int,
    memo: String
):
  private lazy val memoCache =
    val stop = memo.indexOf("}}")
    if memo.startsWith("{{") && stop >= 2 then
      val t = memo.substring(2, stop).split(",").toSet
      val m = memo.substring(stop + 2)
      (m, t)
    else (memo, Set.empty)
  def memoString: String = memoCache._1
  def tags: Set[String] = memoCache._2
  def hasTag(tag: String): Boolean = tags.contains(tag)
  def modifyMemoString(s: String): Appoint =
    copy(memo = Appoint.constructMemo(s, tags))
  def modifyTags(tags: Set[String]): Appoint =
    copy(memo = Appoint.constructMemo(memoString, tags))

object Appoint:
  def create(
      appointId: Int,
      appointTimeId: Int,
      patientName: String,
      patientId: Int,
      memoString: String,
      tags: Set[String]
  ): Appoint =
    Appoint(
      appointId,
      appointTimeId,
      patientName,
      patientId,
      constructMemo(memoString, tags)
    )
  def constructMemo(s: String, ts: Set[String]): String =
    if ts.isEmpty then s
    else "{{" + ts.mkString(",") + "}}" + s
  given DataId[Appoint] = _.appointId
  val modelSymbol = "appoint"
  given ModelSymbol[Appoint] with
    def getSymbol: String = modelSymbol

case class Hotline(message: String, sender: String, recipient: String)

object Hotline:
  val modelSymbol = "hotline"
  given ModelSymbol[Hotline] with
    def getSymbol: String = modelSymbol

enum WaitState(val code: Int, val label: String):
  case WaitExam extends WaitState(0, "診待")
  case InExam extends WaitState(1, "診中")
  case WaitCashier extends WaitState(2, "会待")
  case WaitDrug extends WaitState(3, "薬待")
  case WaitReExam extends WaitState(4, "再待")

object WaitState:
  def fromCode(code: Int): WaitState =
    WaitState.values.find(_.code == code).get

case class Wqueue(visitId: Int, waitState: WaitState)

object Wqueue:
  val modelSymbol = "wqueue"
  given ModelSymbol[Wqueue] with
    def getSymbol: String = modelSymbol
  given DataId[Wqueue] = _.visitId

case class VisitAttributes(
    val futanWari: Option[Int] = None
):
  def asStore: Option[String] =
    if futanWari.isEmpty then None
    else Some(this.asJson.toString)

object VisitAttributes:
  given Encoder[VisitAttributes] = deriveEncoder[VisitAttributes]

case class Visit(
    visitId: Int,
    patientId: Int,
    visitedAt: LocalDateTime,
    shahokokuhoId: Int,
    roujinId: Int,
    kouhi1Id: Int,
    kouhi2Id: Int,
    kouhi3Id: Int,
    koukikoureiId: Int,
    attributesStore: Option[String]
):
  def kouhiIds: List[Int] = List(kouhi1Id, kouhi2Id, kouhi3Id).filter(_ > 0)
  given Decoder[VisitAttributes] = deriveDecoder[VisitAttributes]
  def attributes: VisitAttributes =
    attributesStore match {
      case None => VisitAttributes()
      case Some(src) =>
        decode(src) match {
          case Right(a) => a
          case Left(ex) => throw ex
        }
    }
  def futanWariOverride: Option[Int] =
    attributes.futanWari
  def visitedDate: LocalDate = visitedAt.toLocalDate

object Visit:
  given Encoder[VisitAttributes] = deriveEncoder[VisitAttributes]
  def encodeAttributes(value: VisitAttributes): String =
    value.asJson.toString
  val modelSymbol = "visit"
  given ModelSymbol[Visit] with
    def getSymbol: String = modelSymbol
  given DataId[Visit] = _.visitId

case class Text(
    textId: Int,
    visitId: Int,
    content: String,
    memo: Option[String],
)

case class HokenIdSet(
    shahokokuhoId: Int,
    koukikoureiId: Int,
    roujinId: Int,
    kouhi1Id: Int,
    kouhi2Id: Int,
    kouhi3Id: Int
):
  def kouhiIds(): List[Int] = 
    List(kouhi1Id, kouhi2Id, kouhi3Id).filter(_ > 0)

enum DrugCategory(val code: Int):
  case Naifuku extends DrugCategory(0)
  case Tonpuku extends DrugCategory(1)
  case Gaiyou extends DrugCategory(2)

object DrugCategory:
  def fromCode(code: Int): DrugCategory =
    DrugCategory.values.find(_.code == code).get

case class Drug(
    drugId: Int,
    visitId: Int,
    iyakuhincode: Int,
    amount: Double,
    usage: String,
    days: Int,
    categoryStore: Int,
    prescribed: Boolean
):
  lazy val category: DrugCategory = DrugCategory.fromCode(categoryStore)

case class Shinryou(
    shinryouId: Int,
    visitId: Int,
    shinryoucode: Int,
    memo: Option[String] = None,
)

enum ConductKind(val code: Int, val rep: String):
  case HikaChuusha extends ConductKind(0, "皮下・筋肉注射")
  case JoumyakuChuusha extends ConductKind(1, "静脈注射")
  case OtherChuusha extends ConductKind(2, "その他の注射")
  case Gazou extends ConductKind(3, "画像")

object ConductKind:
  def fromCode(kind: Int): ConductKind =
    ConductKind.values.find(_.code == kind).get

case class Conduct(
    conductId: Int,
    visitId: Int,
    kindStore: Int
):
  lazy val kind: ConductKind = ConductKind.fromCode(kindStore)

case class ConductDrug(
    conductDrugId: Int,
    conductId: Int,
    iyakuhincode: Int,
    amount: Double
)

case class ConductShinryou(
    conductShinryouId: Int,
    conductId: Int,
    shinryoucode: Int,
    memo: Option[String] = None,
)

case class ConductKizai(
    conductKizaiId: Int,
    conductId: Int,
    kizaicode: Int,
    amount: Double
)

case class GazouLabel(
    conductId: Int,
    label: String
)

case class Charge(
    visitId: Int,
    charge: Int
)

object Charge:
  val modelSymbol = "charge"
  given ModelSymbol[Charge] with
    def getSymbol: String = modelSymbol

case class Payment(
    visitId: Int,
    amount: Int,
    paytime: LocalDateTime
)

object Payment:
  val modelSymbol = "payment"
  given ModelSymbol[Payment] with
    def getSymbol: String = modelSymbol

trait EffectivePeriodProvider[T]:
  def getValidFrom(t: T): LocalDate
  def getValidUpto(t: T): ValidUpto
  def isValidAt(t: T, d: LocalDate): Boolean =
    DateUtil.isValidAt(d, getValidFrom(t), getValidUpto(t).value)

case class IyakuhinMaster(
    iyakuhincode: Int,
    yakkacode: String,
    name: String,
    yomi: String,
    unit: String,
    yakkaStore: String,
    madoku: String,
    kouhatsu: String,
    zaikei: String,
    validFrom: LocalDate,
    validUpto: ValidUpto,
    senteiRyouyouKubun: Int,
    ippanmei: String,
    ippanmeicode: String,
    choukiShuusaihinKanren: Int,
    ippanmeiShohouKasanKubun: Int,
):
  def yakka: Double = yakkaStore.toDouble

object IyakuhinMaster:
  given EffectivePeriodProvider[IyakuhinMaster] with
    def getValidFrom(t: IyakuhinMaster): LocalDate = t.validFrom
    def getValidUpto(t: IyakuhinMaster): ValidUpto = t.validUpto

case class ShinryouMaster(
    shinryoucode: Int,
    name: String,
    tensuuStore: String,
    tensuuShikibetsu: String,
    shuukeisaki: String,
    houkatsukensa: String,
    oushinkubun: String,
    kensagroup: String,
    validFrom: LocalDate,
    validUpto: ValidUpto,
    chuukasan: Option[Int],
    chuukasanOrder: Option[String],
):
  def tensuu: Int = tensuuStore.toDouble.toInt

case class KizaiMaster(
    kizaicode: Int,
    name: String,
    yomi: String,
    unit: String,
    kingakuStore: String,
    validFrom: LocalDate,
    validUpto: ValidUpto
):
  def kingaku: Double = kingakuStore.toDouble

case class ByoumeiMaster(
    shoubyoumeicode: Int,
    name: String
)

case class ShuushokugoMaster(
    shuushokugocode: Int,
    name: String
):
  def isPrefix: Boolean =
    shuushokugocode < 8000

trait PatientIdProvider[T]:
  def getPatientId(t: T): Int

type Hoken = Shahokokuho | Koukikourei | Roujin | Kouhi

object Hoken:
  enum HokenKind:
    case ShahokokuhoKind, KoukikoureiKind, RoujinKind, KouhiKind

  extension (h: Hoken)
    def isShahokokuho: Boolean = h.isInstanceOf[Shahokokuho]
    def isKoukikourei: Boolean = h.isInstanceOf[Koukikourei]
    def isRoujin: Boolean = h.isInstanceOf[Roujin]
    def isKouhi: Boolean = h.isInstanceOf[Kouhi]

    def asShahokokuho: Shahokokuho = h.asInstanceOf[Shahokokuho]
    def asKoukikourei: Koukikourei = h.asInstanceOf[Koukikourei]
    def asRoujin: Roujin = h.asInstanceOf[Roujin]
    def asKouhi: Kouhi = h.asInstanceOf[Kouhi]

    def tryCastAsShahokokuho: Option[Shahokokuho] = Option.when(h.isShahokokuho)(h.asShahokokuho)
    def tryCastAsKoukikourei: Option[Koukikourei] = Option.when(h.isKoukikourei)(h.asKoukikourei)
    def tryCastAsRoujin: Option[Roujin] = Option.when(h.isRoujin)(h.asRoujin)
    def tryCastAsKouhi: Option[Kouhi] = Option.when(h.isKouhi)(h.asKouhi)

    def isValidAt(d: LocalDate): Boolean =
      h match {
        case h: Shahokokuho => h.isValidAt(d)
        case h: Koukikourei => h.isValidAt(d)
        case h: Roujin => h.isValidAt(d)
        case h: Kouhi => h.isValidAt(d)
      }

  object HokenKind:
    def apply(hoken: Hoken): HokenKind =
      hoken match {
        case _: Shahokokuho => ShahokokuhoKind
        case _: Koukikourei => KoukikoureiKind
        case _: Roujin => RoujinKind
        case _: Kouhi => KouhiKind
      }

  def idOf(hoken: Hoken): Int =
      hoken match {
        case h: Shahokokuho => h.shahokokuhoId
        case h: Koukikourei => h.koukikoureiId
        case h: Roujin => h.roujinId
        case h: Kouhi => h.kouhiId
      }

  def codeOf(hoken: Hoken): String =
      hoken match {
        case _: Shahokokuho => "shahokokuho"
        case _: Koukikourei => "koukikourei"
        case _: Roujin => "roujin"
        case _: Kouhi => "kouhi"
      }

  case class HokenId(kind: HokenKind, id: Int)

  object HokenId:
    def apply(hoken: Hoken): HokenId =
      HokenId(HokenKind(hoken), idOf(hoken))

case class Shahokokuho(
    shahokokuhoId: Int,
    patientId: Int,
    hokenshaBangou: Int,
    hihokenshaKigou: String,
    hihokenshaBangou: String,
    honninStore: Int,
    validFrom: LocalDate,
    validUpto: ValidUpto,
    koureiStore: Int,
    edaban: String
):
  def koureiFutanWari: Option[Int] =
    if koureiStore == 0 then None
    else Some(koureiStore)
  def validUptoOption: Option[LocalDate] = validUpto.value
  def isHonnin: Boolean = honninStore > 0
  def isValidAt(at: LocalDate): Boolean =
    validFrom <= at && at <= validUpto

object Shahokokuho:
  given DataId[Shahokokuho] = _.shahokokuhoId
  val modelSymbol = "shahokokuho"
  given shahokokuhoModelSymbol: ModelSymbol[Shahokokuho] with
    def getSymbol: String = modelSymbol
  given EffectivePeriodProvider[Shahokokuho] with
    def getValidFrom(t: Shahokokuho) = t.validFrom
    def getValidUpto(t: Shahokokuho) = t.validUpto
  given PatientIdProvider[Shahokokuho] = _.patientId
  given RepProvider[Shahokokuho] = t =>
    HokenRep.shahokokuhoRep(
      t.hokenshaBangou,
      t.koureiFutanWari
    )

case class Roujin(
    roujinId: Int,
    patientId: Int,
    shichouson: Int,
    jukyuusha: Int,
    futanWari: Int,
    validFrom: LocalDate,
    validUpto: ValidUpto
):
  def validUptoOption: Option[LocalDate] = validUpto.value
  def isValidAt(at: LocalDate): Boolean =
    validFrom <= at && at <= validUpto

object Roujin:
  given DataId[Roujin] = _.roujinId
  val modelSymbol = "roujin"
  given ModelSymbol[Roujin] with
    def getSymbol: String = modelSymbol
  given EffectivePeriodProvider[Roujin] with
    def getValidFrom(t: Roujin) = t.validFrom
    def getValidUpto(t: Roujin) = t.validUpto
  given PatientIdProvider[Roujin] = _.patientId
  given RepProvider[Roujin] = t => HokenRep.roujinRep(t.futanWari)

case class Koukikourei(
    koukikoureiId: Int,
    patientId: Int,
    hokenshaBangou: String,
    hihokenshaBangou: String,
    futanWari: Int,
    validFrom: LocalDate,
    validUpto: ValidUpto
):
  def validUptoOption: Option[LocalDate] = validUpto.value
  def isValidAt(at: LocalDate): Boolean =
    validFrom <= at && at <= validUpto

object Koukikourei:
  given DataId[Koukikourei] = _.koukikoureiId
  val modelSymbol = "koukikourei"
  given ModelSymbol[Koukikourei] with
    def getSymbol: String = modelSymbol
  given EffectivePeriodProvider[Koukikourei] with
    def getValidFrom(t: Koukikourei) = t.validFrom
    def getValidUpto(t: Koukikourei) = t.validUpto
  given PatientIdProvider[Koukikourei] = _.patientId
  given RepProvider[Koukikourei] = t => HokenRep.koukikoureiRep(t.futanWari)

case class Kouhi(
    kouhiId: Int,
    futansha: Int,
    jukyuusha: Int,
    validFrom: LocalDate,
    validUpto: ValidUpto,
    patientId: Int,
    memo: Option[String],
):
  def validUptoOption: Option[LocalDate] = validUpto.value
  def isValidAt(at: LocalDate): Boolean =
    validFrom <= at && at <= validUpto

object Kouhi:
  given DataId[Kouhi] = _.kouhiId
  val modelSymbol = "kouhi"
  given ModelSymbol[Kouhi] with
    def getSymbol: String = modelSymbol
  given EffectivePeriodProvider[Kouhi] with
    def getValidFrom(t: Kouhi) = t.validFrom
    def getValidUpto(t: Kouhi) = t.validUpto
  given PatientIdProvider[Kouhi] = _.patientId
  given RepProvider[Kouhi] = t => HokenRep.kouhiRep(t.futansha)

case class MeisaiSectionData(
    section: MeisaiSection,
    entries: List[MeisaiSectionItem]
):
  def subtotal: Int = entries.map(_.total).sum

case class Meisai(
    items: List[MeisaiSectionData],
    futanWari: Int,
    charge: Int
):
  def totalTen: Int = Meisai.calcTotalTen(items)

object Meisai:
  def calcTotalTen(items: List[MeisaiSectionData]): Int =
    items.map(_.subtotal).sum

case class ClinicInfo(
    name: String,
    postalCode: String,
    address: String,
    tel: String,
    fax: String,
    todoufukencode: String,
    tensuuhyoucode: String,
    kikancode: String,
    homepage: String,
    doctorName: String,
    doctorLastName: String,
    doctorFirstName: String,
)

// should be 'case class', because == operator is used
case class ScannerDevice(
    deviceId: String,
    name: String,
    description: String
)

case class FileInfo(
    name: String,
    createdAt: LocalDateTime,
    size: Long
)

object FileInfo:
  val epoch: LocalDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
  def fromTimestamp(ts: FiniteDuration): LocalDateTime =
    epoch.plusSeconds(ts.toSeconds)

enum DiseaseEndReason(val code: String, val label: String):
  case NotEnded extends DiseaseEndReason("N", "継続")
  case Cured extends DiseaseEndReason("C", "治癒")
  case Stopped extends DiseaseEndReason("S", "中止")
  case Dead extends DiseaseEndReason("D", "死亡")

case class Disease(
    diseaseId: Int,
    patientId: Int,
    shoubyoumeicode: Int,
    startDate: LocalDate,
    endDate: ValidUpto,
    endReasonStore: String
):
  def endReason: DiseaseEndReason =
    DiseaseEndReason.values.find(_.code == endReasonStore).get

case class DiseaseAdj(
    diseaseAdjId: Int,
    diseaseId: Int,
    shuushokugocode: Int
)

case class DiseaseEnterData(
  patientId: Int,
  byoumeicode: Int,
  startDate: LocalDate,
  adjCodes: List[Int]
)

case class PrescExample(
  prescExampleId: Int,
  iyakuhincode: Int,
  masterValidFrom: LocalDate,
  amount: String,
  usage: String,
  days: Int,
  category: Int,
  comment: String
)

case class Onshi(
  visitId: Int,
  kakunin: String,
)

case class HokenLists(
  shahokokuhoList: List[Shahokokuho],
  koukikoureiList: List[Koukikourei],
)

case class UsageMaster(
    usage_code: String,
    kubun_code: String,
    kubun_name: String,
    detail_kubun_code: String,
    detail_kubun_name: String,
    timing_code: String,
    timing_name: String,
    usage_name: String,
    usage_code_kubun: String,
    tonpuku_condition: String,
    admin_timing: String,
    admin_time: String,
    admin_interval: String,
    admin_location: String,
    usage_kana: String,
)
