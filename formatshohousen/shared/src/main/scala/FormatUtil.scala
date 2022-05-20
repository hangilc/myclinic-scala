package dev.myclinic.scala.formatshohousen

import dev.myclinic.scala.util.ZenkakuUtil
import FormatPattern.*

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

  def parsePart(s: String): (Part, List[String]) =
    val pp = itemStartPattern.replaceFirstIn(s, zenkakuSpace)
    val lines = pp.linesIterator.toList
    val (pre, post) = lines.span(s => s.startsWith(zenkakuSpace))
    val strippedLines = pre.map(removeLeadingSpaces(_))
    val (cs, ts) = post.partition(_.startsWith(commandStart))
    val (lcs, gcs) = cs.partition(_.startsWith(localCommandStart))
    val item = Item.parse(strippedLines)
    (Part(item, ts, lcs), gcs)

  // def splitToSubparts(p: String): (List[String], List[String]) =
  //   val pp = itemStartPattern.replaceFirstIn(p, zenkakuSpace)
  //   val lines = pp.linesIterator.toList
  //   val (pre, post) = lines.span(s => s.startsWith(zenkakuSpace))
  //   (pre.map(removeLeadingSpaces(_)), post)

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

  def zenkakuSpaceToHankakuSpace(s: String): String =
    s.map {
      case `zenkakuSpaceChar` => ' '
      case c => c
    }

  def hankakuSpaceToZenkakuSpace(s: String): String =
    s.map {
      case ' ' => zenkakuSpaceChar
      case c => c
    }

  def renderForPrint(shohou: String): String =
    shohou.map {
      case `softNewlineChar` => '\n'
      case `softBlankChar` => '　'
      case `softSpaceChar` => '　'
      case c => c
    }

  val lineWithLeadingBlanksPattern = raw"^($space+)($notSpace.*)".r

  def renderForDisp(shohou: String): String =
    shohou.flatMap {
      case `softNewlineChar` => "\n"
      case `softBlankChar` => zenkakuSpace
      case `softSpaceChar` => ""
      case c => c.toString
    }

  def renderForEdit(shohou: String): String =
    shohou.flatMap {
      case `softNewlineChar` => ""
      case `softBlankChar` => ""
      case `softSpaceChar` => ""
      case c => c.toString
    }

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

    
