package dev.myclinic.scala.formatshohousen

object RegexPattern:
  val zenkakuSpace = "　"
  val space: String = s"[ ${zenkakuSpace}]"
  val notSpace: String = s"[^ ${zenkakuSpace}]"
  val digits: String = "[0-9０-９]"
  val digitsPeriod: String = "[0-9０-９.．]"