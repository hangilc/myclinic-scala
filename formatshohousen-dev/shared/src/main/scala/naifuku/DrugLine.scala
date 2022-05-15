package dev.myclinic.scala.formatshohousen.naifuku

import dev.myclinic.scala.formatshohousen.FormatContext

case class DrugLine(name: String, amount: String):
  def format(pre: String, ctx: FormatContext): String =
    NaifukuUtil.formatLine(pre, name, amount, ctx)

