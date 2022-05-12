package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext

class NaifukuMulti() extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    ???

object NaifukuMulti:
  def tryParse(lead: String, more: List[String]): Option[Formatter] =
    val lines = lead :: more
    val drugsSrc = lines.init
    val usageSrc = lines.last
    drugsSrc.map(NaifukuUtil.tryParseDrugLine(_)).sequence
