package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.util.ZenkakuUtil.toZenkaku
import dev.myclinic.scala.formatshohousen.FormatUtil

case class NaifukuSimple(
    name: String,
    amount: String,
    unit: String,
    usage: String,
    days: String,
    daysUnit: String
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val indexRep: String = FormatUtil.indexRep(index, ctx.totalItems)
    val blankRep: String = zenkakuSpace * indexRep.size
    List(
      formatLine(indexRep, name, amount + unit, ctx),
      formatLine(blankRep, usage, days + daysUnit, ctx)
    ).mkString("\n")

  def formatLine(
      pre: String,
      left: String,
      right: String,
      ctx: FormatContext
  ): String =
    val tabRem = ctx.tabPos - (pre.size + left.size)
    (
      if tabRem > 0 then Some(pre + left + (zenkakuSpace * tabRem) + right)
      else None
    ).filter(_.size <= ctx.lineSize)
      .getOrElse(
        FormatUtil.softSplitLine(
          pre,
          left + zenkakuSpace + right,
          ctx.lineSize
        )
      )

object NaifukuSimple:
  val firstPattern = raw"(.*$notSpace)$space+($digitsPeriod+)($notSpace+).*".r
  val secondPattern =
    raw"$space+(分$digits.*$notSpace)$space+($digits+)(日分)$space*".r

  def tryParse(h: String, t: List[String]): Option[NaifukuSimple] =
    h :: t match {
      case List(
            firstPattern(name, amount, unit),
            secondPattern(usage, days, daysUnit)
          ) =>
        Some(
          NaifukuSimple(
            name,
            amount,
            unit,
            usage,
            days,
            daysUnit
          )
        )
      case _ => None
    }
