package dev.myclinic.scala.model

enum Sex(val code: String, val rep: String):
  case Male extends Sex("M", "男")
  case Female extends Sex("F", "女")

object Sex:
  def fromCode(code: String): Sex =
    Sex.values.find(_.code == code).get
