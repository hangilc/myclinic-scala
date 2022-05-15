package dev.myclinic.scala.formatshohousen

object RegexPattern:
  val zenkakuSpaceChar = '　'
  val zenkakuSpace = zenkakuSpaceChar.toString
  val space: String = s"[ ${zenkakuSpace}]"
  val notSpace: String = s"[^ ${zenkakuSpace}]"
  val digit: String = "[0-9０-９]"
  val digits: String = s"$digit+"
  val digitOrPeriod: String = "[0-9０-９.．]"

  val drugNameRegex = s"$notSpace.*$notSpace"
