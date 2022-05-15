package dev.myclinic.scala.formatshohousen

import FormatPattern.*

trait Item:
  def format(index: Int, ctx: FormatContext): List[String]

case class DrugItem(
  drugs: List[DrugPart],
  usage: UsagePart,
  more: List[String]
) extends Item:
  def format(index: Int, ctx: FormatContext): List[String] =
    ???

case class FallbackItem(
  lines: List[String]
) extends Item:
  def format(index: Int, ctx: FormatContext): List[String] =
    ???

object Item:
  val unit = "(?:錠|カプセル|ｇ|ｍｇ|包|ｍＬ|ブリスター|瓶|個|キット|枚|パック|袋)"
  val chunk = s"$notSpace(?:.*$notSpace)?"
  val days1 = s"$digit+日分"
  val days2 = s"$digit+日分"
  val drugPattern = s"($chunk)$space+((?:１回)?$digitOrPeriod+$unit(?:$chunk)?)$space*".r
  val daysPart = s"$digit+(?:日|回)分"
  val usagePattern = s"($chunk)$space+($daysPart(?:$chunk)?)$space*".r

  def parseDrugPart(src: String): Option[DrugPart] =
    src match {
      case drugPattern(name, amount) => Some(DrugPart(name, amount))
      case _ => None
    }

  def parseUsagePart(src: String): Option[UsagePart] =
    src match {
      case usagePattern(usage, days) => Some(UsagePart(usage, days))
      case _ => None
    }

  def parse(lines: List[String]): Item =
    val (drugs, rest) = FormatUtil.span(lines, parseDrugPart _)
    rest match {
      case h :: t if drugs.size > 0 =>
        val usage = parseUsagePart(h).getOrElse(UsagePart(h, ""))
        DrugItem(drugs, usage, t)
      case _ => fallbackItem(lines)
    }

  def fallbackItem(lines: List[String]): Item = FallbackItem(lines)

  