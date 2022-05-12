package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.util.ZenkakuUtil.toZenkaku
import dev.myclinic.scala.formatshohousen.FormatUtil

case class NaifukuSimple(
  drug: DrugLine,
  usage: UsageLine
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val indexRep: String = FormatUtil.indexRep(index, ctx.totalItems)
    val blankRep: String = FormatUtil.blankRep(ctx.totalItems)
    List(
      drug.format(indexRep, ctx),
      usage.format(blankRep, ctx)
    ).mkString("\n")

object NaifukuSimple:
  // val firstRegex = raw"(.*$notSpace)$space+($digitsPeriod+)($notSpace+)"
  // val firstPattern = s"$firstRegex$space*".r
  // val secondRegex = raw"$space+(分$digits.*$notSpace)$space+($digits+)(日分)$space*"
  // val secondPattern = secondRegex.r

  def tryParse(h: String, ts: List[String]): Option[NaifukuSimple] =
    ts match {
      case List(t) =>
        for
          drug <- NaifukuUtil.tryParseDrugLine(h)
          usage <- NaifukuUtil.tryParseUsageLine(t)
        yield NaifukuSimple(drug, usage)
      case _ => None
    }

  val oneLinePattern = (NaifukuUtil.drugRegex + s"$space+" + NaifukuUtil.usageRegex).r
  
  def tryParseOneLine(s: String, t: List[String]): Option[NaifukuSimple] =
    if t.isEmpty then
      s match {
        case oneLinePattern(name, amount, usage, days) =>
          val drugLine = DrugLine(name, amount)
          val usageLine = UsageLine(usage, days)
          Some(new NaifukuSimple(drugLine, usageLine))
        case _ => None
      }
    else None
