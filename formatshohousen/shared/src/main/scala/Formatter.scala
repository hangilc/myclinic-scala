package dev.myclinic.scala.formatshohousen

trait Formatter:
  def format(src: Int, ctx: FormatContext): String