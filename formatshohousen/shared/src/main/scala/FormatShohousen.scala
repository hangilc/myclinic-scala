package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil
import FormatUtil.{softNewline, softBlank, commandStart}
import dev.myclinic.scala.formatshohousen.naifuku.*
import dev.myclinic.scala.formatshohousen.gaiyou.*
import dev.myclinic.scala.formatshohousen.tonpuku.*
import RegexPattern.*

object FormatShohousen:
  val itemStartPattern = raw"(?m)^$digit+[)）][ 　\n]*".r
  val contLinePattern = raw"^$space.*".r
  val leadingSpaces = s"^$space+".r

  def splitToParts(s: String): List[String] =
    val src: String =
      s.flatMap(c =>
        if c == softNewline || c == softBlank then "" else c.toString
      )
      s.map(ZenkakuUtil.toZenkakuCharExcluding(commandStart))

    val starts: List[Int] =
      itemStartPattern.findAllMatchIn(src).toList.map(_.start)
    val ends = starts.drop(1) :+ s.size
    starts.zip(ends).map { case (start, end) =>
      s.substring(start, end).strip
    }

  def splitToSubparts(p: String): Subparts =
    val pp = itemStartPattern.replaceFirstIn(p, "")
    val lines = pp.linesIterator.toList
    val leadLine = lines.headOption.map(_.trim).getOrElse("")
    val (moreLines, rest) =
      lines.drop(1).span(contLinePattern.matches(_))
    val moreLinesStripped =
      moreLines.map(s => leadingSpaces.replaceFirstIn(s, ""))
    val g: Map[String, List[String]] =
      rest.groupBy[String](s =>
        if s.startsWith(commandStart.toString) then "c" else "t"
      )
    val (trails, commands) =
      (g.getOrElse("t", List.empty), g.getOrElse("c", List.empty))
    Subparts(leadLine, moreLinesStripped, trails, commands)

  def prepareForFormat(s: String): (String, List[String]) =
    val subs = splitToSubparts(s)
    val lead = subs.leadLine
    val more = subs.lines
    (lead, more)

  def parseItem(s: String): Formatter =
    val (lead, more) = prepareForFormat(s)
    NaifukuSimple
      .tryParse(lead, more)
      .orElse(NaifukuMulti.tryParse(lead, more))
      .orElse(NaifukuSimple.tryParseOneLine(lead, more))
      .orElse(GaiyouShippu.tryParse(lead, more))
      .orElse(GaiyouShippu.tryParseOneLine(lead, more))
      .orElse(GaiyouCream.tryParse(lead, more))
      .orElse(GaiyouCream.tryParseOneLine(lead, more))
      .orElse(GaiyouDrop.tryParse(lead, more))
      .orElse(GaiyouDrop.tryParseOneLine(lead, more))
      .orElse(TonpukuTimes.tryParse(lead, more))
      .orElse(TonpukuTimes.tryParseOneLine(lead, more))
      .orElse(TonpukuTimes2.tryParse(lead, more))
      .orElse(TonpukuTotal.tryParse(lead, more))
      .orElse(MiscFormatter.tryParse(lead, more))
      .getOrElse(FallbackFormatter(lead, more))

  def parseItemWith(
      s: String,
      f: (String, List[String]) => Option[Formatter]
  ): Option[Formatter] =
    val (lead, more) = prepareForFormat(s)
    f(lead, more)
