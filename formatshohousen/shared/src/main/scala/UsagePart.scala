package dev.myclinic.scala.formatshohousen

case class UsagePart(usage: String, daysTimes: String):
  def format(pre: String, ctx: FormatContext): String =
    Formatter.tabFormat(pre, usage, daysTimes, ctx.tabPos - 2, ctx.lineSize)