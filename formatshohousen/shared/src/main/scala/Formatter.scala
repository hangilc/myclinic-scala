package dev.myclinic.scala.formatshohousen

import FormatPattern.*
import FormatUtil.{softBlank, softNewline}

object Formatter:
  def tabFormat(pre: String, left: String, right: String, tabPos: Int, lineSize: Int): String =
    if right.isEmpty then softFormat(pre, left, lineSize)
    else
      val cw = lineSize - pre.size
      val tpos = tabPos - pre.size
      def fmt1(left: String): Option[String] =
        val rem = tabPos - left.size
        if rem > 0 then Some(left + zenkakuSpace * rem + right)
        else None
      def fmt2(left: String): Option[String] =
        val rem = cw - left.size - right.size
        if rem > 0 then Some(left + zenkakuSpace * rem + right)
        else None
      def iter(left: String, fmt: String => Option[String], cur: List[String]): List[String] =
        fmt(left) match {
          case None => 
            val (pre, post) = splitAtAdjusting(left, cw)
            iter(post, fmt, cur :+ pre)
          case Some(s) => cur :+ s
        }
      val fmt: String => Option[String] =
        if right.size <= lineSize - tabPos then fmt1 else fmt2
      val bpre = FormatUtil.softBlank * pre.size
      indent(pre, bpre, iter(left, fmt, List.empty)).mkString(softNewline)

  def softFormat(pre: String, s: String, lineSize: Int): String =
    val cw = lineSize - pre.size
    def fmt(s: String): Option[String] =
      if s.size <= cw then Some(s) else None
    def iter(s: String, cur: List[String]): List[String] =
      fmt(s) match {
        case None =>
          val (pre, post) = splitAtAdjusting(s, cw)
          iter(post, cur :+ pre)
        case Some(s) => cur :+ s
      }
    val bpre = FormatUtil.softBlank * pre.size
    indent(pre, bpre, iter(s, List.empty)).mkString(softNewline)

  def indent(pre1: String, pre2: String, lines: List[String]): List[String] =
    lines match {
      case Nil => List.empty
      case h :: t =>
        (pre1 + h) :: t.map(s => pre2 + s)
    }

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
