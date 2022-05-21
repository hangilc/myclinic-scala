package dev.myclinic.scala.formatshohousen

trait Shohou[T]:
  def formatForDisp(t: T): String
  def formatForPrint(t: T): String
  def formatForSave(t: T): String
