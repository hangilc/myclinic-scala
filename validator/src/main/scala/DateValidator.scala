package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.Patient
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
import dev.myclinic.scala.util.KanjiDate
import java.time.LocalDate
import scala.util.{Success, Failure, Try}

object DateValidator:
  sealed trait DateError:
    def message: String
  object NoGengouError extends DateError:
    def message: String = "元号の入力がありません。"
  object InvalidGengouError extends DateError:
    def message: String = "元号が不適切です。"
  object EmptyNenError extends DateError:
    def message: String = "年が入力されていません。"
  object NonIntegerNenError extends DateError:
    def message: String = "年の入力が整数でありません。"
  object NonPositiveNenError extends DateError:
    def message: String = "年の入力が正の整数でありません。"
  object InvalidNenError extends DateError:
    def message: String = "年の入力の値が不適切です。"
  object EmptyMonthError extends DateError:
    def message: String = "月が入力されていません。"
  object NonIntegerMonthError extends DateError:
    def message: String = "月の入力が整数でありません。"
  object NonPositiveMonthError extends DateError:
    def message: String = "月の入力が正の整数でありません。"
  object InvalidMonthError extends DateError:
    def message: String = "月の入力の値が不適切です。"
  object EmptyDayError extends DateError:
    def message: String = "日が入力されていません。"
  object NonIntegerDayError extends DateError:
    def message: String = "日の入力が整数でありません。"
  object NonPositiveDayError extends DateError:
    def message: String = "日の入力が正の整数でありません。"
  object InvalidDayError extends DateError:
    def message: String = "日の入力の値が不適切です。"
  object InvalidDateError extends DateError:
    def message: String = "日付の入力が不適切です。"

  type Result[T] = ValidatedNec[DateError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] = Validators.toEither(r, _.message)

  def validateGengouInput(input: Option[String]): Result[KanjiDate.Gengou] =
    input.fold(
      invalidNec(NoGengouError)
    )(s =>
      KanjiDate.Gengou.findByName(s) match {
        case Some(g) => validNec(g)
        case None    => invalidNec(InvalidGengouError)
      }
    )

  def validateNenInput(input: String): Result[Int] =
    nonEmpty(input, EmptyNenError)
      .andThen(s => isInt(s, NonIntegerNenError))
      .andThen(i => positiveInt(i, NonPositiveNenError))

  def validateMonthInput(input: String): Result[Int] =
    nonEmpty(input, EmptyMonthError)
      .andThen(s => isInt(s, NonIntegerMonthError))
      .andThen(i => positiveInt(i, NonPositiveMonthError))

  def validateDayInput(input: String): Result[Int] =
    nonEmpty(input, EmptyDayError)
      .andThen(s => isInt(s, NonIntegerDayError))
      .andThen(i => positiveInt(i, NonPositiveDayError))

  def validateDate(
      gengouResult: Result[KanjiDate.Gengou],
      nenResult: Result[Int],
      monthResult: Result[Int],
      dayResult: Result[Int]
  ): Result[LocalDate] =
    (gengouResult, nenResult, monthResult, dayResult)
      .mapN((g, n, m, d) => {
        val year = KanjiDate.Gengou.gengouToYear(g, n)
        (year, m, d)
      })
      .andThen { case (y, m, d) =>
        Try(LocalDate.of(y, m, d)) match {
          case Success(date) => validNec(date)
          case Failure(ex)   => invalidNec(InvalidDateError)
        }
      }
