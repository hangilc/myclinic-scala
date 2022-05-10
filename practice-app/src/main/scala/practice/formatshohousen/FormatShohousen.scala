package dev.myclinic.scala.web.practiceapp.practice.formatshohousen

import scala.util.matching.Regex
import dev.myclinic.scala.util.ZenkakuUtil

trait ShohousenItem:
  def render(index: Int, ctx: FormatContext): List[String]

case class FormattedShohousen(
    prefix: String,
    items: List[ShohousenItem] = List.empty,
    tail: String = ""
):
  def render: String =
    val alphaResult = prefix + "\n" + itemsToString + tail
    ZenkakuUtil.toZenkaku(alphaResult)

  def itemsToString: String =
    val ctx = FormatContext(items.size)
    items.zipWithIndex.flatMap { case (item, index) =>
      item.render(index + 1, ctx)
    }.mkString

object FormatShohousen:
  def format(src: String): String =
    parse(src).fold(src)(_.render)

  def parse(src: String): Option[FormattedShohousen] =
    val alpha = ZenkakuUtil.toHankaku(src)
    val lines: List[String] = alpha.linesIterator.toList
    parsePrefix(lines)
      .flatMap(parseItems _)
      .flatMap(parseTail _)

  def parsePrefix(
      lines: List[String]
  ): Option[(FormattedShohousen, List[String])] =
    println(("lines", lines))
    lines match {
      case (a @ "院外処方") :: (b @ "Rp)") :: tail =>
        Some(FormattedShohousen(List(a, b).mkString("\n")), tail)
      case _ => None
    }

  def parseItems(
      f: FormattedShohousen,
      lines: List[String]
  ): Option[(FormattedShohousen, List[String])] =
    lines match {
      case line :: tail =>
        if line.trim.isEmpty then parseItems(f, tail)
        else if itemStartPattern.matches(line) then
          val strippedHead = itemStartPattern.replaceFirstIn(line, "")
          val (pre, post) = tail.span(isItemCont _)
          val item = linesToItem(strippedHead , pre)
          Some(f.copy(items = f.items :+ item), post)
        else Some(f, lines)
      case _ => Some(f, List.empty)
    }

  val itemStartPattern: Regex = raw"^[0-9]+\)".r

  def isItemCont(s: String): Boolean = s.startsWith(" ")

  def linesToItem(h: String, t: List[String]): ShohousenItem =
    toNaifuku(h, t)
      .orElse(toTonpuku(h, t))
      .orElse(toGaiyou(h, t))
      .getOrElse(toFallback(h, t))

  def toNaifuku(h: String, t: List[String]): Option[ShohousenItem] =
    NaifukuItem.tryParse(h, t)

  def toTonpuku(h: String, t: List[String]): Option[ShohousenItem] =
    TonpukuItem.tryParse(h, t)

  def toGaiyou(h: String, t: List[String]): Option[ShohousenItem] =
    GaiyouItem.tryParse(h, t)

  def toFallback(h: String, t: List[String]): ShohousenItem =
    new FallbackItem(h, t)

  def parseTail(
      f: FormattedShohousen,
      lines: List[String]
  ): Option[FormattedShohousen] =
    Some(f.copy(tail = lines.mkString("\n")))

  def indexToZenkaku(index: Int): String =
    ZenkakuUtil.convertToZenkakuDigits(index.toString) + "）"

  val leadingSpacesPattern: Regex = raw"[ 　]+".r

  def stripLeadingSpaces(s: String): String =
    leadingSpacesPattern.replaceFirstIn(s, "")

  def itemLeadingSpaces(index: Int): String =
    if index < 10 then "  "
    else "   "

