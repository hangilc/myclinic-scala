package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.*
import dev.myclinic.java.{HoukatsuKensa, HoukatsuKensaKind}
import scala.collection.mutable.ListBuffer

sealed trait MeisaiUnit:
  def tanka: Int
  def count: Int
  def label: String
  def merge(that: MeisaiUnit): Option[MeisaiUnit]
  def section: MeisaiSection

  def total: Int = tanka * count
  def toItem: MeisaiSectionItem = MeisaiSectionItem(tanka, count, label)

object MeisaiUnit:
  def fromShinryou(shinryou: ShinryouEx): MeisaiUnit =
    val houkatsuKind = HoukatsuKensaKind.fromCode(shinryou.master.houkatsukensa)
    houkatsuKind match {
      case HoukatsuKensaKind.NONE => SimpleShinryouUnit(shinryou)
      case _ => ???
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
