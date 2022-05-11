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
    firstLine(index, ctx).mkString("\n")

  def firstLine(index: Int, ctx: FormatContext): List[String] =
    val irep = FormatUtil.indexRep(index, ctx.totalItems)
    val rem = ctx.tabPos - (name.size + amount.size + unit.size)
    val fmt = irep + name + (zenkakuSpace * rem) + amount + unit
    if fmt.size <= ctx.lineSize then
      List(fmt)
    else
      ???

  def secondLine()


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
            toZenkaku(name),
            toZenkaku(amount),
            toZenkaku(unit),
            toZenkaku(usage),
            toZenkaku(days),
            toZenkaku(daysUnit)
          )
        )
      case _ => None
    }
