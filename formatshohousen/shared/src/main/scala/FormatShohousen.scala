package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil
import FormatUtil.{softNewline, softBlank, commandStart}

object FormatShohousen:
  val itemStartPattern = raw"(?m)^[0-9０-９]+[)）]".r
  val leadLinePattern = raw"[0-9０-９]+[)）]\s*(.*)".r
  val contLinePattern = raw"^[ 　].*".r

  def splitToParts(s: String): List[String] =
    val src: String = 
      s.flatMap(c => if c == softNewline || c == softBlank then "" else c.toString)
      s.map(ZenkakuUtil.toZenkakuCharExcluding(commandStart))

    val starts: List[Int] =
      itemStartPattern.findAllMatchIn(src).toList.map(_.start)
    val ends = starts.drop(1) :+ s.size
    starts.zip(ends).map { case (start, end) =>
      s.substring(start, end).strip
    }

  def splitToSubparts(p: String): Subparts =
    val lines: List[String] = p.linesIterator.toList
    val leadLine: String = lines.headOption
      .flatMap(line => leadLinePattern.findPrefixMatchOf(line))
      .map(m => m.group(1))
      .getOrElse("")
    val (moreLines, rest) = lines.tail.span(contLinePattern.matches(_))
    val g: Map[String, List[String]] =
      rest.groupBy[String](s => if s.startsWith(commandStart.toString) then "c" else "t")
    val (trails, commands) =
      (g.getOrElse("t", List.empty), g.getOrElse("c", List.empty))
    Subparts(leadLine, moreLines, trails, commands)

