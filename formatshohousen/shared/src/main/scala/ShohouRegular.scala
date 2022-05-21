package dev.myclinic.scala.formatshohousen

import fs2.Stream
import cats.syntax.validated
import cats.kernel.Monoid

case class ShohouRegular(
  parts: List[Part],
  commands: List[String]
):
  def formatForDisp: String =
    val ctx = FormatContext(parts.size)
    ShohouRegular.prolog + ((parts.zipWithIndex.flatMap {
      case (part, ord) => part.formatForDisp(ord+1, ctx)
    }) ++ commands).mkString("\n")

  def formatForPrint: String =
    val ctx = FormatContext(parts.size)
     ShohouRegular.prolog + ((parts.zipWithIndex.flatMap {
      case (part, ord) => part.formatForPrint(ord+1, ctx)
    }) ++ commands).mkString("\n")

object ShohouRegular:
  given shohouRegularShohou: Shohou[ShohouRegular] with
    def formatForDisp(t: ShohouRegular): String = t.formatForDisp
    def formatForPrint(t: ShohouRegular): String = t.formatForPrint

  def parse(src: String): ShohouRegular =
    Stream(src)
      .map(FormatUtil.stripShohouProlog _)
      .map(FormatUtil.prepareForFormat _)
      .flatMap(s => Stream.emits(FormatUtil.splitToParts(s)))
      .map(FormatUtil.parsePart _)
      .fold((List.empty[PartTemplate], List.empty[String])) {
        case ((tmpls, cmds), (tmpl, cmd)) => (tmpls :+ tmpl, cmds ++ cmd)
      }
      .map((tmpls, commands) => 
        val gc = GlobalCommand(commands)
        val parts = 
          if gc.raw then tmpls.map(_.toRawPart)
          else tmpls.map(_.toPart)
        ShohouRegular(parts, commands)
      ).toList(0)

  val prolog = FormatUtil.shohouProlog
