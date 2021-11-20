package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.*
import dev.myclinic.java.{HoukatsuKensa, HoukatsuKensaKind}
import scala.collection.mutable.ListBuffer
import java.time.LocalDate

trait RcptGroup:
  def toItem(): MeisaiSectionItem

class SimpleShinryouGroup(shinryou: ShinryouEx) extends RcptGroup:
  private var count: Int = 1
  def canAdd(that: ShinryouEx): Boolean = shinryou.master.shinryoucode == that.master.shinryoucode
  def add(that: ShinryouEx): Unit = count += 1
  def toItem(): MeisaiSectionItem =
    MeisaiSectionItem(shinryou.master.tensuu, count, shinryou.master.name)

case class HoukatsuKensaGroup(houkatsu: HoukatsuKensa, at: LocalDate, kind: HoukatsuKensaKind) extends RcptGroup:
  private val members: ListBuffer[ShinryouEx] = ListBuffer()
  def canAdd(that: ShinryouEx): Boolean = kind == HoukatsuKensaKind.fromCode(that.master.houkatsukensa)
  def add(shinryou: ShinryouEx): Unit = members :+ shinryou
  def tanka: Int = 
    val opt = houkatsu.calcTen(kind, members.size, at)
    if opt.isPresent() then opt.get
    else
      members.map(v => v.master.tensuu).sum
  def label: String = members.map(_.master.name).mkString("、")
  def toItem(): MeisaiSectionItem = 
    MeisaiSectionItem(tanka, 1, label)
