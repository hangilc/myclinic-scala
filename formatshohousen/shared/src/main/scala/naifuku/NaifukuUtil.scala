package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.RegexPattern.*
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil

object NaifukuUtil:
  val unit = "(?:錠|カプセル|Ｃ|ｇ|ｍｇ|包|Ｔ|ｐ|Ｐ)"
  val drugRegex = raw"($drugNameRegex)$space+($digitOrPeriod+$unit$notSpace*)"
  val drugPattern = s"$drugRegex$space*".r
  val usageRegex = raw"(分$digit.*$notSpace)$space+($digit+日分$notSpace*)"
  val usagePattern = s"$usageRegex$space*".r

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
