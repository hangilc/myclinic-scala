package dev.myclinic.scala.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered
import io.circe.*
import io.circe.syntax.*
import io.circe.parser.decode
import io.circe.generic.semiauto._

case class ValidUpto(value: Option[LocalDate])

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

  given Ordering[AppointTime] with
    def compare(a: AppointTime, b: AppointTime): Int =
      val cmp: Int = summon[Ordering[LocalDate]].compare(a.date, b.date)
      if cmp == 0 then
        summon[Ordering[LocalTime]].compare(a.fromTime, b.fromTime)
      else cmp

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

case class Hotline(message: String, sender: String, recipient: String)

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

case class VisitAttributes(
    val futanWari: Option[Int] = None
)

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

object Visit:
  given Encoder[VisitAttributes] = deriveEncoder[VisitAttributes]
  def encodeAttributes(value: VisitAttributes): String =
    value.asJson.toString

case class Text(
    textId: Int,
    visitId: Int,
    content: String
)

case class Drug(
    drugId: Int,
    visitId: Int,
    iyakuhincode: Int,
    amount: Double,
    usage: String,
    days: Int,
    category: Int,
    prescribed: Boolean
)

case class Shinryou(
    shinryouId: Int,
    visitId: Int,
    shinryoucode: Int
)

case class Conduct(
    conductId: Int,
    visitId: Int,
    kind: Int
)

case class ConductDrug(
    conductDrugId: Int,
    conductId: Int,
    iyakuhincode: Int,
    amount: Double
)

case class ConductShinryou(
    conductShinryouId: Int,
    conductId: Int,
    shinryoucode: Int
)

case class ConductKizai(
    conductKizaiId: Int,
    conductId: Int,
    kizaicode: Int,
    amount: Double
)

case class Charge(
    visitId: Int,
    charge: Int
)

case class Payment(
    visitId: Int,
    amount: Int,
    paytime: LocalDateTime
)

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
    validUpto: ValidUpto
):
  def yakka: Double = yakkaStore.toDouble

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
    validUpto: ValidUpto
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

case class Shahokokuho(
    shahokokuhoId: Int,
    patientId: Int,
    hokenshaBangou: Int,
    hihokanshaKigou: String,
    hihokenshaBangou: String,
    honnin: Int,
    validFrom: LocalDate,
    validUpto: ValidUpto,
    kourei: Int,
    edaban: String
)

case class Roujin(
    roujinId: Int,
    patientId: Int,
    shichouson: Int,
    jukyuusha: Int,
    futanWari: Int,
    validFrom: LocalDate,
    validUpt: ValidUpto
)

case class Koukikourei(
    koukikoureiId: Int,
    patientId: Int,
    hokenshaBangou: String,
    hihokenshaBangou: String,
    futanWari: Int,
    validFrom: LocalDate,
    validUpt: ValidUpto
)

case class Kouhi(
    kouhiId: Int,
    futansha: Int,
    jukyuusha: Int,
    validFrom: LocalDate,
    validUpto: ValidUpto,
    patientId: Int
)

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
    doctorName: String
)
