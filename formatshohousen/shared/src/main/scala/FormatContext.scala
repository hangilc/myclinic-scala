package dev.myclinic.scala.formatshohousen

case class FormatContext(
  totalItems: Int,
  tabPos: Int = 21,
  lineSize: Int = 31,
  altPosShift: Int = 0
):
  def altTabPos: Int = tabPos + altPosShift
