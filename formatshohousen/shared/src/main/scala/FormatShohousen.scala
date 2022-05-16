package dev.myclinic.scala.formatshohousen

object FormatShohousen:
  def format(src: String): String =
    val zs = FormatUtil.prepareForFormat(src)
    zs