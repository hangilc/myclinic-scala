package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.*
import dev.myclinic.java.{HoukatsuKensa, HoukatsuKensaKind}
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
      case HoukatsuKensaKind.NONE => SimpleShinryouUnit(shinryou)
      case _ =>
        HoukatsuKensaUnit(
          houkatsuKind,
          at,
          List(shinryou),
          1
        )
    }
  def fromConduct(conduct: ConductEx): MeisaiUnit =
    ???

case class SimpleShinryouUnit(shinryou: ShinryouEx, count: Int = 1)
    extends MeisaiUnit:
  def tanka: Int = shinryou.master.tensuu
  def label: String = shinryou.master.name
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case u: SimpleShinryouUnit =>
        if shinryoucode == u.shinryoucode then
          Some(this.copy(count = count + u.count))
        else None
      case _ => None
    }
  def section: MeisaiSection =
    Shuukei.shuukeisakiToMeisaiSection(shinryou.master.shuukeisaki)
  def shinryoucode: Int = shinryou.master.shinryoucode

case class HoukatsuKensaUnit(
    kind: HoukatsuKensaKind,
    at: LocalDate,
    items: List[ShinryouEx],
    val count: Int
)(using houkatsuKensa: HoukatsuKensa)
    extends MeisaiUnit:
  def tanka: Int =
    houkatsuKensa.calcTen(kind, items.size, at).toScala match {
      case Some(ten) => ten
      case None      => items.map(_.master.tensuu).sum
    }
  def label: String = items.map(_.master.name).mkString("、")
  def merge(that: MeisaiUnit): Option[MeisaiUnit] =
    that match {
      case HoukatsuKensaUnit(k, a, is, c) if k == kind && a == at =>
        Some(HoukatsuKensaUnit(kind, at, items ++ is, count + c))
      case _ => None
    }
  def section: MeisaiSection = MeisaiSection.Kensa
