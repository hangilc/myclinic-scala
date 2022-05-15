package dev.myclinic.scala.formatshohousen

object FormatPattern:
  val zenkakuSpaceChar = '　'
  val zenkakuSpace = zenkakuSpaceChar.toString
  val space: String = zenkakuSpace
  val notSpace: String = s"[^${zenkakuSpace}]"
  val digit: String = "[０-９]"
  val zenkakuPeriodChar = '．'
  val digitOrPeriod: String = s"[０-９$zenkakuPeriodChar]"
  val endedWithNotSpace = s"$notSpace(?:.*$notSpace)?"
