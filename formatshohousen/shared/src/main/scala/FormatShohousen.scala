package dev.myclinic.scala.formatshohousen

object FormatShohousen:
  def isShohou(s: String): Boolean =
    FormatUtil.isShohou(s)

  def parse(src: String): Shohou =
    ShohouRaw.tryParse(src).getOrElse(ShohouRegular.parse(src))
