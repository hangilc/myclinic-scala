package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil

object NaifukuUtil:
  val unit = "(?:錠|カプセル|ｇ|ｍｇ|包|Ｔ)"
  val drugRegex = raw"($drugNameRegex)$space+($digitOrPeriod+$unit$notSpace*)"
  val drugPattern = s"$drugRegex$space*".r
  val usageRegex = raw"(分$digit.*$notSpace)$space+($digit+日分)$space*"
  val usagePattern = usageRegex.r

  def tryParseDrugLine(s: String): Option[DrugLine] =
    s match {
      case drugPattern(name, amount) => Some(DrugLine(name, amount))
      case _ => None
    }

  def tryParseUsageLine(s: String): Option[UsageLine] =
    s match {
      case usagePattern(usage, days) => Some(UsageLine(usage, days))
      case _ => None
    }

  def formatLine(
      pre: String,
      left: String,
      right: String,
      ctx: FormatContext
  ): String =
    FormatUtil.formatTabLine(pre, left, right, ctx)

  // def formatLine(
  //     pre: String,
  //     left: String,
  //     right: String,
  //     ctx: FormatContext
  // ): String =
  //   val tabRem = ctx.tabPos - (pre.size + left.size)
  //   (
  //     if tabRem > 0 then Some(pre + left + (zenkakuSpace * tabRem) + right)
  //     else None
  //   ).filter(_.size <= ctx.lineSize)
  //     .getOrElse(
  //       FormatUtil.softSplitLine(
  //         pre,
  //         left + zenkakuSpace + right,
  //         ctx.lineSize
  //       )
  //     )
