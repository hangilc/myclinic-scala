package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq
import dev.myclinic.scala.util.KanjiDate
import dev.myclinic.scala.util.KanjiDate.Gengou
import dev.myclinic.scala.util.ZenkakuUtil
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.language.implicitConversions
import scala.util.matching.Regex
import cats.*
import cats.syntax.*
import cats.implicits.*
import cats.data.Validated.{validNec, invalidNec, condNec}
import cats.data.ValidatedNec
import java.time.LocalDate
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import cats.data.Validated.Valid
import cats.data.Validated.Invalid

class DateInput(gengouList: List[Gengou] = Gengou.list):
  val eInput: HTMLInputElement = inputText()
  val ele: HTMLElement = div(cls := "domq-date-input-wrapper")(
    eInput(cls := "domq-date-input")
  )
  def validate(): Either[String, LocalDate] =
    DateInput.validateDateInput(eInput.value) match {
      case Valid(d) => Right(d)
      case Invalid(err) => Left(err.toList.map(_.message).mkString("\n"))
    }

object DateInput:
  val pat: Regex = raw"(\D*)\s*(\d+)\s*年\s*(\d+)\s*月\s*(\d+)日".r

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

  class Seireki

  def validateGengou(src: String): Result[Gengou | Seireki] =
    println(("gengou src", src))
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
    println(("validate src", src))
    val input = ZenkakuUtil.convertZenkakuDigits(src.trim)
    println(("validate input", input))
    input match {
      case DateInput.pat(gengou, nen, month, day) => {
        (
          validateGengou(gengou),
          validateNen(nen),
          validateMonth(month),
          validateDay(day)
        ).tupled.andThen((g: Gengou|Seireki, n: Int, m: Int, d: Int) => {
          val year = g match {
            case _: Seireki => n
            case g: Gengou => Gengou.gengouToYear(g, n)
          }
          Try(LocalDate.of(year, m, d)) match {
            case Success(date) => validNec(date)
            case Failure(_) => invalidNec(InvalidDateError)
          }
        })
      }
      case _ => invalidNec(DateInput.InvalidFormatError)
    }
