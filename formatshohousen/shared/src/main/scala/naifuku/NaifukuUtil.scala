package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.RegexPattern.*

object NaifukuUtil:
  val drugPattern = raw"(.*$notSpace)$space+($digitsPeriod+$notSpace+).*".r
  val usagePattern =
    raw"$space+(分$digits.*$notSpace)$space+($digits+日分)$space*".r

  def tryParseDrugLine(s: String): Option[DrugLine] =
    s match {
      case drugPattern(name, amount) => Some(DrugLine(name, amount))
      case _ => None
    }

  def tryParseUsageLine(s: String): Option[UsageLine] =
    s match {
      case usagePattern(usage, days) => Some(UsageLine(usage, days))
    }