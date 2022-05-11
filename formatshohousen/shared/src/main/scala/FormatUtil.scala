package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil.toZenkaku

object FormatUtil:
  def indexRep(index: Int, totalItems: Int): String =
    val w = if totalItems < 10 then 1 else 2
    val rep = String.format(s"%${w}d)", index)
    toZenkaku(rep)
