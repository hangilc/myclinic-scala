package dev.myclinic.scala.util

import java.text.NumberFormat

object NumberUtil:
  def withComma(ival: Int): String =
    NumberFormat.getIntegerInstance().format(ival)