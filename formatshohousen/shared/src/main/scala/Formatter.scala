package dev.myclinic.scala.formatshohousen

import FormatPattern.*
import FormatUtil.{softBlank, softNewline}

object Formatter:
  def tabFormat(pre: String, left: String, right: String, ctx: FormatContext): String =
    val rightPadded: String =
      val padSize = (ctx.lineSize - ctx.tabPos - right.size).max(0)
      right + (zenkakuSpace * padSize)
    val rem = ctx.lineSize - (pre.size + left.size + rightPadded.size)
    if rem > 0 then
      pre + left + (zenkakuSpace * rem) + rightPadded
    else 
      val cw = ctx.lineSize - pre.size
      val lines = splitAdjusting(pre + left, cw)
      val last = lines.last
      val rem = cw - (last.size + rightPadded.size)
      val ss = 
        if rem > 0 then 
          lines.init :+ (last + (zenkakuSpace * rem) + rightPadded)
        else
          lines ++ rightJustified(rightPadded, cw)
      convertToSoft(ss, pre.size)

  def convertToSoft(lines: List[String], preSize: Int): String =
    val bpre = zenkakuSpace * preSize
    val delim = softNewline + bpre
    lines.mkString(delim)

  def rightJustified(s: String, lineSize: Int): List[String] =
    val lines = splitAdjusting(s, lineSize)
    val last = lines.last
    val just = zenkakuSpace * (lineSize - last.size) + last
    lines.init :+ just

  def splitAdjusting(s: String, lineSize: Int): List[String] =
    def iter(s: String, acc: List[String]): List[String] =
      val (a, b)= splitAtAdjusting(s, lineSize)
      if b.isEmpty then acc :+ a
      else
        iter(b, acc :+ a)
    iter(s, List.empty)

  def splitAtAdjusting(s: String, at: Int): (String, String) =
    val (pre, post) = adjustSplit.tupled(s.splitAt(at))
    (pre, post)

  def adjustSplit(a: String, b: String): (String, String) =
    val (pre, tail) = b.span(c => c == zenkakuSpaceChar)
    (a ++ pre, tail)
