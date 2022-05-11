package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil.toZenkaku
import FormatUtil.{softNewline, softBlank}
import RegexPattern.zenkakuSpaceChar

object FormatUtil:
  val softNewline = '~'
  val softBlank = '^'
  val commandStart = '@'

  def indexRep(index: Int, totalItems: Int): String =
    val w = if totalItems < 10 then 1 else 2
    val rep = String.format(s"%${w}d)", index)
    toZenkaku(rep)

  def softSplitLine(
      preBlank: String,
      line: String,
      lineSize: Int
  ): String =
    val softPre = softBlank.toString * preBlank.size
    def iter(s: String, lines: List[String]): List[String] =
      if s.size <= lineSize then lines :+ s
      else
        val (a, b) = adjustSplit.tupled(s.splitAt(lineSize))
        iter(
          softPre + b,
          lines :+ a + softNewline
        )
    iter(preBlank + line, List.empty).mkString

  def adjustSplit(a: String, b: String): (String, String) =
    val (pre, tail) = b.span(c => c == ' ' || c == zenkakuSpaceChar)
    (a ++ pre, tail)

