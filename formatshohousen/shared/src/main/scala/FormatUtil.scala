package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil
import FormatPattern.*
import dev.myclinic.scala.util.StringUtil

object FormatUtil:
  val softNewlineChar = '~'
  val softNewline = softNewlineChar.toString
  val softBlankChar = '^'
  val softBlank = softBlankChar.toString
  val softSpaceChar = '`'
  val softSpace = softSpaceChar.toString
  val commandStartChar = '@'
  val commandStart = commandStartChar.toString
  val localCommandStart = commandStart + "_"
  val itemStartPattern = raw"(?:^|\n)$digit+[）]$space*".r
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
              case `softSpaceChar`   => ""
              case _               => ZenkakuUtil.toZenkakuChar(c).toString
            }
          )
      })
      .mkString("\n")

  def splitToParts(s: String): (String, List[String]) =
    StringUtil.cut(itemStartPattern, s)

  def removeLeadingSpaces(s: String): String =
    leadingSpacesPattern.replaceFirstIn(s, "")

  def parsePart(s: String): (PartTemplate, List[String]) =
    val pp = itemStartPattern.replaceFirstIn(s, zenkakuSpace)
    val lines = pp.linesIterator.toList
    val (pre, post) = lines.span(s => s.startsWith(zenkakuSpace))
    val strippedLines = pre.map(removeLeadingSpaces(_))
    val (cs, ts) = post.partition(_.startsWith(commandStart))
    val (lcs, gcs) = cs.partition(_.startsWith(localCommandStart))
    (PartTemplate(strippedLines, ts, lcs), gcs)

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

  val shohouStartPattern = raw"^院外処方[ 　]*\nＲｐ）[ 　]*\n".r
  val shohouProlog = "院外処方\nＲｐ）\n"

  def isShohou(s: String): Boolean =
    shohouStartPattern.findFirstIn(s).isDefined

  def stripShohouProlog(s: String): String =
    shohouStartPattern.replaceFirstIn(s, "")

  def prependShohouProlog(s: String): String = 
    shohouProlog + s

  def mapContent(s: String, f: String => String): String =
    val c = stripShohouProlog(s)
    prependShohouProlog(f(c))

    
