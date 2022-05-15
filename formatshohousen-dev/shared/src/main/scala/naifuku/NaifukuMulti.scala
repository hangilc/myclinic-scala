package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil.sequence
import dev.myclinic.scala.formatshohousen.FormatUtil

class NaifukuMulti(
    drugs: List[DrugLine],
    usage: UsageLine,
    more: List[String] = List.empty
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    val drugLines: List[String] = drugs match {
      case Nil => List.empty
      case h :: t =>
        h.format(ipre, ctx) :: t.map(_.format(bpre, ctx))
    }
    val usageLine: String = usage.format(bpre, ctx)
    val moreLines: List[String] =
      more.map(s => FormatUtil.softSplitLine(bpre, s, ctx))
    (drugLines ++ List(usageLine) ++ moreLines).mkString("\n")

object NaifukuMulti:
  def tryParse(lead: String, more: List[String]): Option[Formatter] =
    val lines = lead :: more
    val (drugs, rest) = FormatUtil.span(lines, NaifukuUtil.tryParseDrugLine _)
    for
      uline <- rest.headOption
      usage <- NaifukuUtil.tryParseUsageLine(uline)
    yield new NaifukuMulti(drugs, usage, rest.drop(1))
