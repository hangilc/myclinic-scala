package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Modifier}
import scala.language.implicitConversions
import cats.data.ValidatedNec
import cats.implicits.*
import cats.data.Validated.{validNec, invalidNec, condNec}
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.myclinic.scala.util.KanjiDate
import dev.myclinic.scala.validator.Validators
import dev.myclinic.scala.validator.Validators.*
import scala.util.Success
import scala.util.Try
import scala.util.Failure
import org.scalajs.dom.raw.{HTMLElement}

class DateInput(gengouList: List[KanjiDate.Gengou] = KanjiDate.Gengou.list):
  val eGengouSelect: HTMLElement = select(
    gengouList.map(g => (option(g.name, attr("value") := g.name): Modifier)): _*
  )
  val eNenInput = inputText()
  val eMonthInput = inputText()
  val eDayInput = inputText()
  val ele =
    div(display := "inline-flex", alignItems := "center", cls := "date-input")(
      eGengouSelect(cls := "gengou"),
      eNenInput(cls := "nen"),
      span("年", cls := "label"),
      eMonthInput(cls := "month"),
      span("月", cls := "label"),
      eDayInput(cls := "day"),
      span("日", cls := "label")
    )

  def validate(): Either[String, LocalDate] =
    DateInputValidator
      .validateDateInput(
        DateInputValidator.validateGengouInput(
          eGengouSelect.getSelectedOptionValues.headOption
        ),
        DateInputValidator.validateNenInput(eNenInput.value),
        DateInputValidator.validateMonthInput(eMonthInput.value),
        DateInputValidator.validateDayInput(eDayInput.value)
      )
      .asEither

object DateInputValidator:
  sealed trait DateInputError:
    def message: String
  object NoGengouError extends DateInputError:
    def message: String = "元号の入力がありません。"
  object InvalidGengouError extends DateInputError:
    def message: String = "元号が不適切です。"
  object EmptyNenError extends DateInputError:
    def message: String = "年が入力されていません。"
  object NonIntegerNenError extends DateInputError:
    def message: String = "年の入力が整数でありません。"
  object NonPositiveNenError extends DateInputError:
    def message: String = "年の入力が正の整数でありません。"
  object InvalidNenError extends DateInputError:
    def message: String = "年の入力の値が不適切です。"
  object EmptyMonthError extends DateInputError:
    def message: String = "月が入力されていません。"
  object NonIntegerMonthError extends DateInputError:
    def message: String = "月の入力が整数でありません。"
  object NonPositiveMonthError extends DateInputError:
    def message: String = "月の入力が正の整数でありません。"
  object InvalidMonthError extends DateInputError:
    def message: String = "月の入力の値が不適切です。"
  object EmptyDayError extends DateInputError:
    def message: String = "日が入力されていません。"
  object NonIntegerDayError extends DateInputError:
    def message: String = "日の入力が整数でありません。"
  object NonPositiveDayError extends DateInputError:
    def message: String = "日の入力が正の整数でありません。"
  object InvalidDayError extends DateInputError:
    def message: String = "日の入力の値が不適切です。"
  object InvalidDateError extends DateInputError:
    def message: String = "日付の入力が不適切です。"

  type Result[T] = ValidatedNec[DateInputError, T]

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

  def validateDateInput(
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
