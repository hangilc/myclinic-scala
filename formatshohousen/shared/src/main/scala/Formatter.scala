package dev.myclinic.scala.formatshohousen

trait Formatter:
  def format(src: String, ctx: FormatContext): String