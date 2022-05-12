package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.FormatContext

case class UsageLine(usage: String, days: String):
  def format(pre: String, ctx: FormatContext): String =
    NaifukuUtil.formatLine(pre, usage, days, ctx)
