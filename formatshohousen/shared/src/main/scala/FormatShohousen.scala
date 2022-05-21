package dev.myclinic.scala.formatshohousen

import ShohouRaw.given

object FormatShohousen:
  def isShohou(s: String): Boolean =
    FormatUtil.isShohou(s)

  def parse[T: Shohou](src: String): T =
    ShohouRaw(src)
    // ShohouRaw.tryParse(src).getOrElse(ShohouRegular.parse(src))
