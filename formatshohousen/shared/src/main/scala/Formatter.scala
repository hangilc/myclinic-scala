package dev.myclinic.scala.formatshohousen

import FormatPattern.*
import FormatUtil.{softBlank, softNewline, softSpace}

object Formatter:
  def leadingZenkakuPattern = raw"^$zenkakuSpace+".r
  def trailingZenkakuPattern = raw"$zenkakuSpace+$$".r

  def zenkakuTrim(s: String): String =
    val t = leadingZenkakuPattern.replaceFirstIn(s, "")
    trailingZenkakuPattern.replaceFirstIn(t, "")

  def breakLine(s: String, lineSize: Int): List[String] =
    def iter(s: String, cur: List[String]): List[String] =
      val t = zenkakuTrim(s)
      if s.size <= lineSize then cur :+ t
      else 
        val (pre, post) = t.splitAt(lineSize)
        iter(t, cur :+ pre)
    iter(s, List.empty)

  def breakLines(lines: List[String], lineSize: Int): List[String] =
    lines.map(breakLine(_, lineSize)).flatten

  def tryTabLine(left: String, right: String, tabStop: Int, lineSize: Int): Option[String] =
    def rightFitsInColumn: Boolean = right.size <= lineSize - tabStop
    def rem: Int = tabStop - left.size
    if rightFitsInColumn && rem > 0 then 
      Some(left + zenkakuSpace * rem + right)
    else None

  def breakTabLine(left: String, right: String, tabStop: Int, lineSize: Int): List[String] =
    val lines = breakLine(left, lineSize)
    val last = lines.last
    tryTabLine(last, right, tabStop, lineSize) match {
      case Some(last) => lines.init :+ last
      case None =>
        if right.size >= lineSize then
          lines.init ++ breakLine(s"$last$zenkakuSpace$right", lineSize)
        else if right.size >= lineSize - tabStop then
          val rem = lineSize - left.size - right.size
          if rem > 0 then 
            lines.init :+ (left + zenkakuSpace * rem + right)
          else 
            val rem = lineSize - right.size
            lines :+ (zenkakuSpace * rem + right)
        else
          lines :+ (zenkakuSpace * tabStop + right)
    }

  def indent(pre1: String, pre2: String, lines: List[String]): List[String] =
    lines match {
      case Nil => List.empty
      case h :: t =>
        (pre1 + h) :: t.map(s => pre2 + s)
    }
