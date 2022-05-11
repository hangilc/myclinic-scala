package dev.myclinic.scala.formatshohousen

object RegexPattern:
  val space: String = "[ 　]"
  val notSpace: String = "[^ 　]"
  val digits: String = "[0-9０-９]"
  val digitsPeriod: String = "[0-9０-９.．]"