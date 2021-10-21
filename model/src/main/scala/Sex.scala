package dev.myclinic.scala.model

enum Sex(code: String, rep: String):
  case Male extends Sex("M", "男")
  case Female extends Sex("F", "女")
