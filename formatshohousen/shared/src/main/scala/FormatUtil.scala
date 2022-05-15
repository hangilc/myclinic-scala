package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil
import FormatPattern.*

object FormatUtil:
  val softNewlineChar = '~'
  val softNewline = softNewlineChar.toString
  val softBlankChar = '^'
  val softBlank = softBlankChar.toString
  val commandStartChar = '@'
  val itemStartPattern = raw"(?m)^$digit+[）]$space*".r
  val leadingSpacesPattern = s"^$space+".r

  def convertToZenkaku(s: String): String =
    s.flatMap(c =>
      if c == softNewlineChar || c == softBlankChar then "" else c.toString
    )
    s.map(ZenkakuUtil.toZenkakuCharExcluding(commandStartChar))

  def splitToParts(s: String): List[String] =
    val src: String = convertToZenkaku(s)
    val starts: List[Int] =
      itemStartPattern.findAllMatchIn(src).toList.map(_.start)
    val ends = starts.drop(1) :+ s.size
    starts.zip(ends).map { case (start, end) =>
      s.substring(start, end).strip
    }

  def removeLeadingSpaces(s: String): String =
    leadingSpacesPattern.replaceFirstIn(s, "")

  
  def splitToSubparts(p: String): (List[String], List[String]) =
    val pp = itemStartPattern.replaceFirstIn(p, zenkakuSpace)
    val lines = pp.linesIterator.toList
    val (pre, post) = lines.span(s => s.startsWith(zenkakuSpace))
    (pre.map(removeLeadingSpaces(_)), post)

  def span[A, B](as: List[A], f: A => Option[B]): (List[B], List[A]) =
    def iter(as: List[A], acc: List[B]): (List[B], List[A]) =
      as match {
        case Nil => (acc, List.empty)
        case h :: t =>
          f(h) match {
            case Some(b) => iter(t, acc :+ b)
            case None => (acc, as)
          }
      }
    iter(as, List.empty)


