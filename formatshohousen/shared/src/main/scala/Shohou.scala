package dev.myclinic.scala.formatshohousen

case class Shohou(
  parts: List[Part],
  commands: List[String]
):
  def formatForDisp: String =
    val ctx = FormatContext(parts.size)
    Shohou.prolog + ((parts.zipWithIndex.flatMap {
      case (part, ord) => part.formatForDisp(ord+1, ctx)
    }) ++ commands).mkString("\n")

  def formatForPrint: String =
    val ctx = FormatContext(parts.size)
     Shohou.prolog + ((parts.zipWithIndex.flatMap {
      case (part, ord) => part.formatForPrint(ord+1, ctx)
    }) ++ commands).mkString("\n")

object Shohou:
  def formatForDisp(s: String): String =
    parse(s).formatForDisp

  def formatForPrint(s: String): String =
    parse(s).formatForPrint

  def parse(src: String): Shohou =
    val srcWithoutProlog: String = FormatUtil.stripShohouProlog(src)
    val zs = FormatUtil.prepareForFormat(srcWithoutProlog)
    val partTmplsAndCommands = FormatUtil.splitToParts(zs).map(FormatUtil.parsePart)
    val partTmpls: List[PartTemplate] = partTmplsAndCommands.map(_._1)
    val commands: List[String] = partTmplsAndCommands.map(_._2).flatten
    Shohou(parts, commands)

  def isShohou(s: String): Boolean =
    FormatUtil.isShohou(s)

  val prolog = FormatUtil.shohouProlog
