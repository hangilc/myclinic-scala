package dev.myclinic.scala.formatshohousen

import FormatUtil.*
import RegexPattern.*

class FallbackFormatter(
    leadLine: String,
    moreLines: List[String]
) extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    val irep = FormatUtil.indexRep(index, ctx.totalItems)
    val pre = zenkakuSpace * irep.size
    val lines: List[String] = softSplitLine(irep, leadLine, ctx.lineSize) ::
      moreLines.map(ml => softSplitLine(pre, ml, ctx.lineSize))
    lines.mkString("\n")
