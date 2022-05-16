package dev.myclinic.scala.formatshohousen

case class DrugPart(name: String, amount: String):
  def format(pre: String, ctx: FormatContext): String =
    Formatter.tabFormat(pre, name, amount, ctx)