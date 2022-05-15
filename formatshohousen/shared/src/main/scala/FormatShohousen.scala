package dev.myclinic.scala.formatshohousen

object FormatShohousen:
  def format(src: String): String =
    val zs = FormatUtil.convertToZenkaku(src)
    zs