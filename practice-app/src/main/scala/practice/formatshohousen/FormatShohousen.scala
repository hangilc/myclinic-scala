package dev.myclinic.scala.web.practiceapp.practice.formatshohousen

import scala.util.matching.Regex

trait ShohousenItem:
  def render(index: Int): String

case class FormattedShohousen(
    prefix: String,
    items: List[ShohousenItem] = List.empty,
    tail: String = ""
):
  def render: String =
    prefix + "\n" + itemsToString + tail

  def itemsToString: String = 
    items.zipWithIndex.map {
      case (item, index) => item.render(index + 1)
    }.mkString

object FormatShohousen:
  def format(src: String): String =
    parse(src).fold(src)(_.render)

  def parse(src: String): Option[FormattedShohousen] =
    val lines: List[String] = src.linesIterator.toList
    parsePrefix(lines)
      .flatMap(parseItems _)
      .flatMap(parseTail _)

  def parsePrefix(
      lines: List[String]
  ): Option[(FormattedShohousen, List[String])] =
    lines match {
      case (a @ "院外処方") :: (b @ "Ｒｐ）") :: tail =>
        Some(FormattedShohousen(List(a, b).mkString("\n")), tail)
      case _ => None
    }

  def parseItems(f: FormattedShohousen, lines: List[String]):
    Option[(FormattedShohousen, List[String])] =
      lines match {
        case line :: tail => 
          if line.trim.isEmpty then parseItems(f, tail)
          else if isItemStart(line) then
            val (pre, post) = tail.span(isItemCont _)
            val item = linesToItem(line :: pre)
            Some(f.copy(items = f.items :+ item), post)
          else Some(f, lines)
        case _ => Some(f, List.empty)
      }

  val itemStartPattern: Regex = raw"^[０-９0-9]+[）)]".r

  def isItemStart(s: String): Boolean = itemStartPattern.matches(s)

  def isItemCont(s: String): Boolean = s.startsWith(" ") || s.startsWith("　")

  def linesToItem(lines: List[String]): ShohousenItem =
    new FallbackItem(lines)

  def parseTail(f: FormattedShohousen, lines: List[String]):
    Option[FormattedShohousen] =
      Some(f.copy(tail = lines.mkString("\n")))
