package dev.fujiwara.kanjidate

import KanjiDate.Gengou
import java.time.LocalDate
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object DateParser:
  private val digit = "[0-9０-９]"
  private val alphaDatePattern =
    """([MTSHR])(\d{1,2})\.(\d{1,2})\.(\d{1,2})""".r
  private val kanjiDatePattern =
    raw"(明治|大正|昭和|平成|令和)(${digit}{1,2})年(${digit}{1,2})月(${digit}{1,2})日".r
  private def zenkakuDigitCharToAlpha(ch: Char): Char =
    ch match {
      case '０' => '0'
      case '１' => '1'
      case '２' => '2'
      case '３' => '3'
      case '４' => '4'
      case '５' => '5'
      case '６' => '6'
      case '７' => '7'
      case '８' => '8'
      case '９' => '9'
      case c   => c
    }
  private def zenkakuDigitToAlpha(s: String): String =
    s.map(zenkakuDigitCharToAlpha _)

  private def convertToDate(
      gen: Gengou,
      nen: Int,
      month: Int,
      day: Int
  ): Option[LocalDate] =
    val year = Gengou.gengouToYear(gen, nen)
    Try(LocalDate.of(year, month, day)) match {
      case Success(d) => Some(d)
      case Failure(_) => None
    }

  private def tupled(
      gOpt: Option[Gengou],
      nenOpt: Option[Int],
      monthOpt: Option[Int],
      dayOpt: Option[Int]
  ): Option[(Gengou, Int, Int, Int)] =
    for
      g <- gOpt
      n <- nenOpt
      m <- monthOpt
      d <- dayOpt
    yield (g, n, m, d)

  def parse(src: String): Option[LocalDate] =
    src match {
      case alphaDatePattern(g, n, m, d) =>
        tupled(
          Gengou.findByAlphaChar(g.charAt(0)),
          zenkakuDigitToAlpha(n).toIntOption,
          zenkakuDigitToAlpha(m).toIntOption,
          zenkakuDigitToAlpha(d).toIntOption
        ).flatMap(convertToDate.tupled)
      case kanjiDatePattern(g, n, m, d) =>
        tupled(
          Gengou.findByName(g),
          zenkakuDigitToAlpha(n).toIntOption,
          zenkakuDigitToAlpha(m).toIntOption,
          zenkakuDigitToAlpha(d).toIntOption
        ).flatMap(convertToDate.tupled)
      case _ =>
        Try(LocalDate.parse(src)) match {
          case Success(d) => Some(d)
          case Failure(_) => None
        }
    }
