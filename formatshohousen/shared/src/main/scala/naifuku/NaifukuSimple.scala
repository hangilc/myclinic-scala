package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext

import dev.myclinic.scala.formatshohousen.RegexPattern.*

case class NaifukuSimple(
    name: String,
    amount: String,
    unit: String,
    usage: String,
    days: String,
    daysUnit: String
) extends Formatter:
  def format(index: String, ctx: FormatContext): String =
    ???

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
        Some(NaifukuSimple(name, amount, unit, usage, days, daysUnit))
      case _ => None
    }
