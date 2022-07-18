package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.*
import dev.myclinic.scala.util.ZenkakuUtil
import dev.myclinic.scala.util.FunUtil.*
import dev.myclinic.scala.util.RcptUtil

case class NaifukuUnit(
    key: NaifukuKey,
    drugs: List[(IyakuhinMaster, Double)],
    count: Int
) extends MeisaiUnit:
  import NaifukuUnit.drugLabel
  import RcptUtil.touyakuKingakuToTen
  def label: String =
    drugs.map(t => drugLabel(t._1, t._2, key.days)).mkString("\n")
  val section: MeisaiSection = MeisaiSection.Touyaku
  def tanka: Int = drugs.map { (m, a) =>
    m.yakka * a
  }.sum |> touyakuKingakuToTen |> (_ * key.days)
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case NaifukuUnit(k, ds, c) if k == key && drugs == ds =>
        Some(NaifukuUnit(key, drugs, count + c))
      case NaifukuUnit(k, ds, c) if k == key && c == count =>
        Some(NaifukuUnit(key, drugs ++ ds, c))
      case _ => None
    }

object NaifukuUnit:
  def apply(drug: DrugEx): NaifukuUnit =
    NaifukuUnit(
      NaifukuKey(drug.usage, drug.days),
      List((drug.master, drug.amount)),
      1
    )

  def drugLabel(m: IyakuhinMaster, a: Double, days: Int): String =
    s"${m.name} ${a}${m.unit} ${days}日分"

case class GaiyouUnit(
    master: IyakuhinMaster,
    amount: Double,
    usage: String,
    count: Int
) extends MeisaiUnit:
  def label: String = s"${master.name} ${amount}${master.unit}"
  val section: MeisaiSection = MeisaiSection.Touyaku
  def tanka: Int = master.yakka * amount |> RcptUtil.touyakuKingakuToTen
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case GaiyouUnit(m, a, u, c) if m == master && a == amount && u == usage =>
        Some(GaiyouUnit(master, amount, usage, count + c))
      case _ => None
    }

object GaiyouUnit:
  def apply(drug: DrugEx): GaiyouUnit =
    GaiyouUnit(drug.master, drug.amount, drug.usage, 1)

case class TonpukuUnit(
    usage: String,
    times: Int,
    drugs: List[(IyakuhinMaster, Double)],
    count: Int
) extends MeisaiUnit:
  println(("TonpukuUnit created"))
  def label: String =
    drugs.map(t => TonpukuUnit.drugLabel(t._1, t._2, times)).mkString("\n")
  val section: MeisaiSection = MeisaiSection.Touyaku
  def tanka: Int = drugs.map { (master, amount) =>
    println(("tanka", master.yakka, amount, times))
    master.yakka * amount * times
  }.sum |> RcptUtil.touyakuKingakuToTen
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case TonpukuUnit(u, t, ds, c) if u == usage && t == times && ds == drugs =>
        Some(copy(count = count + c))
      case TonpukuUnit(u, t, ds, c) if u == usage && t == times && c == count =>
        Some(copy(drugs = drugs ++ ds))
      case _ => None
    }

object TonpukuUnit:
  def apply(drug: DrugEx): TonpukuUnit =
    TonpukuUnit(drug.usage, drug.days, List((drug.master, drug.amount)), 1)
  def drugLabel(master: IyakuhinMaster, amount: Double, times: Int): String =
    s"${master.name} １回${amount}${master.unit} ${times}回分"

case class NaifukuKey(usage: String, days: Int)

object NaifukuKey:
  def apply(origUsage: String, days: Int): NaifukuKey =
    new NaifukuKey(normalizeUsage(origUsage), days)

  def normalizeUsage(src: String): String =
    src
      |> (_.trim)
      |> ZenkakuUtil.toHankaku
      |> extractDivide
      |> { (main, divide) => (main, removeBlanks(divide)) }
      |> { (main, divide) => (squashBlanks(main), divide) }
      |> ((m, d) => m + d)

  def extractDivide(src: String): (String, String) =
    val left = src.indexOf("(")
    val right = src.lastIndexOf(")")
    if right > left then
      (
        src.substring(0, left) + src.substring(right + 1),
        src.substring(left, right + 1)
    )
    else (src, "")

  def squashBlanks(src: String): String =
    val regex = raw"\s+".r
    regex.replaceAllIn(src, " ")

  def removeBlanks(src: String): String =
    val regex = raw"\s+".r
    regex.replaceAllIn(src, "")
