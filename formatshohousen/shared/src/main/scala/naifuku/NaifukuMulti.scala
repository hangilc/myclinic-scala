package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext
import dev.myclinic.scala.formatshohousen.FormatUtil.sequence
import dev.myclinic.scala.formatshohousen.FormatUtil

class NaifukuMulti(
    drugs: List[DrugLine],
    usage: UsageLine
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val (ipre, bpre) = FormatUtil.composePre(index, ctx)
    ((drugs match {
      case Nil => List.empty
      case h :: t =>
        h.format(ipre, ctx) :: t.map(_.format(bpre, ctx))
    }) :+ usage.format(bpre, ctx)).mkString("\n")

object NaifukuMulti:
  def tryParse(lead: String, more: List[String]): Option[Formatter] =
    val lines = lead :: more
    val drugsSrc = lines.init
    val usageSrc = lines.last
    for
      ds <- sequence(drugsSrc.map(NaifukuUtil.tryParseDrugLine(_)))
      u <- NaifukuUtil.tryParseUsageLine(usageSrc)
    yield new NaifukuMulti(ds, u)
