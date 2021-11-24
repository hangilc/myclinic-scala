package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.*
import dev.myclinic.java.{
  HoukatsuKensa,
  HoukatsuKensaKind,
  MyclinicConsts,
  RcptCalc
}
import scala.collection.mutable.ListBuffer
import java.time.LocalDate
import scala.jdk.OptionConverters.*

sealed trait MeisaiUnit:
  def tanka: Int
  def count: Int
  def label: String
  def merge(that: MeisaiUnit): Option[MeisaiUnit]
  def section: MeisaiSection

  def total: Int = tanka * count
  def toItem: MeisaiSectionItem = MeisaiSectionItem(tanka, count, label)

object MeisaiUnit:
  def fromShinryou(shinryou: ShinryouEx, at: LocalDate)(using
      HoukatsuKensa
  ): MeisaiUnit =
    val houkatsuKind = HoukatsuKensaKind.fromCode(shinryou.master.houkatsukensa)
    houkatsuKind match {
      case HoukatsuKensaKind.NONE => {
        val section: MeisaiSection =
          Shuukei.shuukeisakiToMeisaiSection(shinryou.master.shuukeisaki)
        SimpleShinryouUnit(section, shinryou.master)
      }
      case _ =>
        HoukatsuKensaUnit(
          houkatsuKind,
          at,
          List(shinryou)
        )
    }
  def fromConduct(conduct: ConductEx): List[MeisaiUnit] =
    val section: MeisaiSection =
      if conduct.kind == MyclinicConsts.ConductKindGazou then
        MeisaiSection.Gazou
      else MeisaiSection.Shochi
    conduct.shinryouList.map(s => SimpleShinryouUnit(section, s.master))
      ++ conduct.drugs.map(d => ConductDrugUnit(section, d))
      ++ conduct.kizaiList.map(k => ConductKizaiUnit(section, k))

case class SimpleShinryouUnit(
    section: MeisaiSection,
    master: ShinryouMaster,
    count: Int = 1
) extends MeisaiUnit:
  def tanka: Int = master.tensuu
  def label: String = master.name
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case u: SimpleShinryouUnit =>
        if shinryoucode == u.shinryoucode then
          Some(this.copy(count = count + u.count))
        else None
      case _ => None
    }
  def shinryoucode: Int = master.shinryoucode

case class HoukatsuKensaUnit(
    kind: HoukatsuKensaKind,
    at: LocalDate,
    items: List[ShinryouEx]
)(using houkatsuKensa: HoukatsuKensa)
    extends MeisaiUnit:
  def tanka: Int =
    houkatsuKensa.calcTen(kind, items.size, at).toScala match {
      case Some(ten) => ten
      case None      => items.map(_.master.tensuu).sum
    }
  def count: Int = 1
  def label: String = items.map(_.master.name).mkString("、")
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case HoukatsuKensaUnit(k, a, is) if k == kind && a == at =>
        Some(HoukatsuKensaUnit(kind, at, items ++ is))
      case _ => None
    }
  def section: MeisaiSection = MeisaiSection.Kensa

case class ConductDrugUnit(
    section: MeisaiSection,
    drug: ConductDrugEx,
    val count: Int = 1
) extends MeisaiUnit:
  def tanka: Int =
    val kingaku: Double = drug.master.yakka * drug.amount
    if section == MeisaiSection.Gazou then rcptCalc.shochiKingakuToTen(kingaku)
    else rcptCalc.touyakuKingakuToTen(kingaku)
  def label: String = s"${drug.master.name} ${amount}${drug.master.unit}"
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case ConductDrugUnit(_, d, c)
          if d.iyakuhincode == iyakuhincode && d.amount == amount =>
        Some(ConductDrugUnit(section, drug, count + c))
      case _ => None
    }
  def iyakuhincode: Int = drug.master.iyakuhincode
  def amount: Double = drug.amount

case class ConductKizaiUnit(
    section: MeisaiSection,
    kizai: ConductKizaiEx,
    val count: Int = 1
) extends MeisaiUnit:
  def tanka: Int =
    val kingaku: Double = kizai.master.kingaku * kizai.amount
    rcptCalc.kizaiKingakuToTen(kingaku)
  def label: String = s"${kizai.master.name} ${amount}${kizai.master.unit}"
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case ConductKizaiUnit(_, k, c)
          if k.kizaicode == kizaicode && k.amount == amount =>
        Some(ConductKizaiUnit(section, kizai, count + c))
      case _ => None
    }
  def kizaicode: Int = kizai.master.kizaicode
  def amount: Double = kizai.amount
