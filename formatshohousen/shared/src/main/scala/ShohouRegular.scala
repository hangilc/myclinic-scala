package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.FunUtil.*

case class ShohouRegular(
  prefix: String = "",
  parts: List[Part] = List.empty,
  commands: List[String] = List.empty
) extends Shohou:
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

  def formatForSave: String = formatForDisp

object ShohouRegular:
  def parse(src: String): ShohouRegular =
    src
      |> FormatUtil.stripShohouProlog
      |> FormatUtil.prepareForFormat
      |> FormatUtil.splitToParts
      |> ((prefix, partStrings) => 
        val init = (List.empty[Part], List.empty[String])
        val (parts, gcs) = partStrings.map(FormatUtil.parsePart _).foldLeft(init) {
          case ((parts, gcs), (tmpl, gc)) => (parts :+ tmpl.toPart, gcs ++ gc)
        }
        ShohouRegular(prefix, parts, gcs)
      )

  val prolog = FormatUtil.shohouProlog
