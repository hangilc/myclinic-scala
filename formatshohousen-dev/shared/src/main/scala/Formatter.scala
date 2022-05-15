package dev.myclinic.scala.formatshohousen

trait Formatter:
  def format(index: Int, ctx: FormatContext): String