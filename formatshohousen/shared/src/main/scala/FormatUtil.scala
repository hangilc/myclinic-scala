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
  val commandPrefixPattern = s"^＠.*?：".r

  def prepareForFormat(s: String): String =
    s.linesIterator
      .map(line => {
        if line.startsWith("@") then line
        else
          line.flatMap(c =>
            c match {
              case `softNewlineChar` => ""
              case `softBlankChar`   => ""
              case _               => ZenkakuUtil.toZenkakuChar(c).toString
            }
          )
      })
      .mkString("\n")

  def splitToParts(s: String): List[String] =
    val src: String = prepareForFormat(s)
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

  def preWidth(totalItems: Int): Int = if totalItems < 10 then 1 else 2

  def indexRep(index: Int, totalItems: Int): String =
    val w = preWidth(totalItems)
    composeIndexRep(index, w)

  def blankRep(totalItems: Int): String =
    val w = preWidth(totalItems)
    composeBlankRep(w)

  def composeIndexRep(index: Int, w: Int): String =
    ZenkakuUtil.toZenkaku(String.format(s"%${w}d)", index))

  def composeBlankRep(w: Int): String =
    zenkakuSpace * (w + 1)

  def composePre(index: Int, ctx: FormatContext): (String, String) =
    val w = preWidth(ctx.totalItems)
    (composeIndexRep(index, w), composeBlankRep(w))

  def span[A, B](as: List[A], f: A => Option[B]): (List[B], List[A]) =
    def iter(as: List[A], acc: List[B]): (List[B], List[A]) =
      as match {
        case Nil => (acc, List.empty)
        case h :: t =>
          f(h) match {
            case Some(b) => iter(t, acc :+ b)
            case None    => (acc, as)
          }
      }
    iter(as, List.empty)

  def renderForPrint(shohou: String): String =
    shohou.map {
      case `softNewlineChar` => '\n'
      case `softBlankChar` => '　'
      case c => c
    }

  def renderForDisp(shohou: String): String =
    shohou.flatMap {
      case `softNewlineChar` => "<br/>\n"
      case `softBlankChar` => " "
      case c => c.toString
    }
    
