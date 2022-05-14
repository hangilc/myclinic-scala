package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.util.ZenkakuUtil.toZenkaku
import dev.myclinic.scala.formatshohousen.FormatUtil

case class NaifukuSimple(
    drug: DrugLine,
    usage: UsageLine,
    more: List[String] = List.empty
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val indexRep: String = FormatUtil.indexRep(index, ctx.totalItems)
    val blankRep: String = FormatUtil.blankRep(ctx.totalItems)
    (List(
      drug.format(indexRep, ctx),
      usage.format(blankRep, ctx)
    ) ++ more.map(s => FormatUtil.softSplitLine(blankRep, s, ctx)))
      .mkString("\n")

object NaifukuSimple:
  val daysPattern = s"$digit+日分".r
  def tryParse(h: String, ts: List[String]): Option[NaifukuSimple] =
    val (pre, post) = ts.span(s => !daysPattern.findFirstIn(s).isDefined)
    for
      last <- post.headOption
      t = (pre :+ last).mkString(zenkakuSpace)
      more = post.drop(1)
      drug <- NaifukuUtil.tryParseDrugLine(h)
      usage <- NaifukuUtil.tryParseUsageLine(t)
    yield NaifukuSimple(drug, usage, more)

  val oneLinePattern =
    (NaifukuUtil.drugRegex + s"$space+" + NaifukuUtil.usageRegex).r

  def tryParseOneLine(s: String, t: List[String]): Option[NaifukuSimple] =
    (s :: t).mkString(zenkakuSpace) match {
      case oneLinePattern(name, amount, usage, days) =>
        val drugLine = DrugLine(name, amount)
        val usageLine = UsageLine(usage, days)
        Some(new NaifukuSimple(drugLine, usageLine))
      case _ => None
    }
