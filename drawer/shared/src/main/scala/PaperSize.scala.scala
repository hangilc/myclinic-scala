package dev.fujiwara.scala.drawer

case class PaperSize(width: Double, height: Double):
  def transpose: PaperSize = new PaperSize(height, width)
  def isLandscape: Boolean = width > height

object PaperSize:
    val A4: PaperSize = new PaperSize(210, 297)
    val A4_Landscape: PaperSize = A4.transpose
    val A5: PaperSize = new PaperSize(148, 210)
    val A5_Landscape: PaperSize = A5.transpose
    val A6: PaperSize = new PaperSize(105, 148)
    val A6_Landscape: PaperSize = A6.transpose
    val B4: PaperSize = new PaperSize(257, 364)
    val B4_Landscape: PaperSize = B4.transpose
    val B5: PaperSize = new PaperSize(182, 257)
    val B5_Landscape: PaperSize = B5.transpose
    val B6: PaperSize = new PaperSize(128, 182)
    val B6_Landscape: PaperSize = B6.transpose

