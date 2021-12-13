package dev.fujiwara.dateinput

import cats.data.ValidatedNec
import cats.implicits.*
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import java.time.LocalDate
import scala.util.{Success, Failure, Try}
import scala.util.matching.Regex
import cats.data.Validated.Valid
import cats.data.Validated.Invalid

object DateInputValidator:
  val pat: Regex = raw"(\D*)\s*(\d+)\s*年\s*(\d+)\s*月\s*(\d+)日.*".r

  sealed trait DateInputError:
    def message: String
  object InvalidGengouError extends DateInputError:
    def message: String = "元号の入力が不適切です。"
  object EmptyNenError extends DateInputError:
    def message: String = "年が入力されていません。"
  object NotNumberNenError extends DateInputError:
    def message: String = "年の入力が数字でありません。"
  object NotIntegerNenError extends DateInputError:
    def message: String = "年の入力が整数でありません。"
  object NotPositiveNenError extends DateInputError:
    def message: String = "年の入力が正の値でありません。"
  object EmptyMonthError extends DateInputError:
    def message: String = "月が入力されていません。"
  object NotNumberMonthError extends DateInputError:
    def message: String = "月の入力が数字でありません。"
  object NotIntegerMonthError extends DateInputError:
    def message: String = "月の入力が整数でありません。"
  object InvalidRangeMonthError extends DateInputError:
    def message: String = "月の入力が適切な範囲でありません。"
  object NotPositiveMonthError extends DateInputError:
    def message: String = "月の入力が正の値でありません。"
  object EmptyDayError extends DateInputError:
    def message: String = "日が入力されていません。"
  object NotNumberDayError extends DateInputError:
    def message: String = "日の入力が数字でありません。"
  object NotIntegerDayError extends DateInputError:
    def message: String = "日の入力が整数でありません。"
  object NotPositiveDayError extends DateInputError:
    def message: String = "日の入力が正の値でありません。"
  object InvalidRangeDayError extends DateInputError:
    def message: String = "日の入力が適切な範囲でありません。"
  object InvalidDateError extends DateInputError:
    def message: String = "日付の入力が不適切です。"
  object InvalidFormatError extends DateInputError:
    def message: String = "日付の入力形式が正しくありません。"

  type Result[T] = ValidatedNec[DateInputError, T]

  extension [T] (r: Result[T])
    def asEither: Either[String, T] =
      r match {
        case Valid(v) => Right(v)
        case Invalid(err) => Left(err.toList.map(_.message).mkString("\n"))
      }

  class Seireki

  def validateGengou(src: String): Result[Gengou | Seireki] =
    if (src == null || src.isEmpty || src == "西暦") then validNec(Seireki())
    else
      Gengou.findByName(src) match {
        case Some(g) => validNec(g)
        case None    => invalidNec(InvalidGengouError)
      }

  def validateNen(src: String): Result[Int] =
    if src == null || src.isEmpty then invalidNec(EmptyNenError)
    else
      Try(src.toInt) match {
        case Failure(_) => invalidNec(NotNumberNenError)
        case Success(ival) =>
          if ival <= 0 then invalidNec(NotPositiveNenError)
          else validNec(ival)
      }

  def validateMonth(src: String): Result[Int] =
    if src == null || src.isEmpty then invalidNec(EmptyMonthError)
    else
      Try(src.toInt) match {
        case Failure(_) => invalidNec(NotNumberMonthError)
        case Success(ival) =>
          if ival <= 0 then invalidNec(NotPositiveMonthError)
          else if ival >= 1 && ival <= 12 then validNec(ival)
          else invalidNec(InvalidRangeMonthError)
      }

  def validateDay(src: String): Result[Int] =
    if src == null || src.isEmpty then invalidNec(EmptyDayError)
    else
      Try(src.toInt) match {
        case Failure(_) => invalidNec(NotNumberDayError)
        case Success(ival) =>
          if ival <= 0 then invalidNec(NotPositiveDayError)
          else if ival >= 1 && ival <= 31 then validNec(ival)
          else invalidNec(InvalidRangeDayError)
      }

  def validateDateInput(src: String): Result[LocalDate] =
    val input = ZenkakuUtil.convertZenkakuDigits(src.trim)
    input match {
      case pat(gengou, nen, month, day) => {
        (
          validateGengou(gengou),
          validateNen(nen),
          validateMonth(month),
          validateDay(day)
        ).tupled.andThen((g: Gengou | Seireki, n: Int, m: Int, d: Int) => {
          val year = g match {
            case _: Seireki => n
            case g: Gengou  => Gengou.gengouToYear(g, n)
          }
          Try(LocalDate.of(year, m, d)) match {
            case Success(date) => validNec(date)
            case Failure(_)    => invalidNec(InvalidDateError)
          }
        })
      }
      case _ => invalidNec(InvalidFormatError)
    }

object ZenkakuUtil:
  val zenkakuDigitToDigit: Char => Char = c =>
    c match {
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
      case _   => c
    }

  extension (f: Char => Char)
    def <+>(g: Char => Char): Char => Char = (c: Char) =>
      val d = f(c)
      if d != c then d
      else g(c)

  def convertChars(src: String, f: Char => Char): String =
    src.toList.map(f).mkString("")

  def convertZenkakuDigits(src: String): String =
    convertChars(src, zenkakuDigitToDigit)
