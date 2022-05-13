package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil.toZenkaku
import FormatUtil.{softNewline, softBlank}
import RegexPattern.{zenkakuSpaceChar, zenkakuSpace}

object FormatUtil:
  val softNewline = '~'
  val softBlank = '^'
  val commandStart = '@'

  def preWidth(totalItems: Int): Int = if totalItems < 10 then 1 else 2

  def indexRep(index: Int, totalItems: Int): String =
    val w = preWidth(totalItems)
    composeIndexRep(index, w)

  def composeIndexRep(index: Int, w: Int): String =
    toZenkaku(String.format(s"%${w}d)", index))

  def blankRep(totalItems: Int): String =
    val w = preWidth(totalItems)
    composeBlankRep(w)

  def composeBlankRep(w: Int): String = 
    zenkakuSpace * (w + 1)

  def composePre(index: Int, ctx: FormatContext): (String, String) =
    val w = preWidth(ctx.totalItems)
    (composeIndexRep(index, w), composeBlankRep(w))

  def softSplitLine(
      prefix: String,
      line: String,
      lineSize: Int
  ): String =
    val softPre = softBlank.toString * prefix.size
    def iter(s: String, lines: List[String]): List[String] =
      if s.size <= lineSize then lines :+ s
      else
        val (a, b) = adjustSplit.tupled(s.splitAt(lineSize))
        iter(
          softPre + b,
          lines :+ a + softNewline
        )
    iter(prefix + line, List.empty).mkString

  def adjustSplit(a: String, b: String): (String, String) =
    val (pre, tail) = b.span(c => c == ' ' || c == zenkakuSpaceChar)
    (a ++ pre, tail)

  def formatAdjusting(indexPre: String, leadLine: String, moreLines: List[String], lineSize: Int): String =
    val preBlank = softBlank.toString * indexPre.size
    val lines: List[String] = softSplitLine(indexPre, leadLine, lineSize) ::
      moreLines.map(line => softSplitLine(preBlank, line, lineSize))
    lines.mkString

  def formatTabLine(
      pre: String,
      left: String,
      right: String,
      ctx: FormatContext
  ): String =
    val tabRem = ctx.tabPos - (pre.size + left.size)
    (
      if tabRem > 0 then Some(pre + left + (zenkakuSpace * tabRem) + right)
      else None
    ).filter(_.size <= ctx.lineSize)
      .getOrElse(
        FormatUtil.softSplitLine(
          pre,
          left + zenkakuSpace + right,
          ctx.lineSize
        )
      )

  def sequence[T](opts: List[Option[T]]): Option[List[T]] =
    opts match {
      case Nil => Some(List.empty)
      case hopt :: topt =>
        for
          h <- hopt
          t <- sequence(topt)
        yield h :: t
    }


