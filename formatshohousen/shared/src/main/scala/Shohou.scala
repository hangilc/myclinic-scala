package dev.myclinic.scala.formatshohousen

case class Shohou(
  parts: List[Part],
  commands: List[String]
):
  def formatForDisp: String =
    

object Shohou:
  def parse(src: String): Shohou =
    val srcWithoutProlog: String = FormatUtil.stripShohouProlog(src)
    val zs = FormatUtil.prepareForFormat(srcWithoutProlog)
    val partsAndCommands = FormatUtil.splitToParts(zs).map(FormatUtil.parsePart)
    val parts: List[Part] = partsAndCommands.map(_._1)
    val commands: List[String] = partsAndCommands.map(_._2).flatten
    Shohou(parts, commands)

  def isShohou(s: String): Boolean =
    FormatUtil.isShohou(s)
