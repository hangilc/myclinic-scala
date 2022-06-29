package dev.fujiwara.validator.section

import cats.*
import cats.syntax.all.*
import cats.data.Validated
import cats.data.Validated.{Valid, Invalid}
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.time.LocalDate
import math.Ordering.Implicits.infixOrderingOps

type ValidatedResult[E, T] = Validated[List[(E, String)], T]
type ValidatedSection[E, T] = ValidatedResult[E, T]

object Implicits:
  extension [E, T](v: ValidatedSection[E, T])
    def asEither: Either[String, T] =
      v.toEither.left.map(_.map(_._2).mkString("\n"))

  extension [E, T](v: Validated[E, T])
    def |>[U](f: T => Validated[E, U]): Validated[E, U] =
      v.andThen(f)

import Implicits.|>

class SectionValidator[E](err: E, name: String):
  type Result[T] = ValidatedSection[E, T]
  def invalid[T](msg: String): Result[T] = Invalid(List((err, msg)))
  def valid[T](t: T): Result[T] = Valid(t)

  def cond[T](test: Boolean, validValue: => T, msg: => String): Result[T] =
    if test then valid(validValue) else invalid(msg)

  def condNot[T](test: Boolean, validValue: => T, msg: => String): Result[T] =
    cond(!test, validValue, msg)

  def notNull(src: String): Result[String] =
    cond(!(src == null), src, s"${name} is null.")

  def notEmpty(src: String): Result[String] =
    cond(!(src == null || src.isEmpty), src, s"${name}が入力されていません。")

  def some[T](src: Option[T]): Result[T] =
    src match {
      case Some(t) => valid(t)
      case None    => invalid(s"${name}が設定されていません。")
    }

  def toInt(src: String): Result[Int] =
    Try(src.toInt) match {
      case Success(i) => valid(i)
      case Failure(_) => invalid(s"${name}を整数に変換できません。")
    }

  def inputToInt(input: String): Result[Int] =
    notEmpty(input) |> toInt

  def positive(value: Int): Result[Int] =
    cond(value > 0, value, s"${name}が正の値でありません。")

  def oneOf[T](value: T, options: List[T]): Result[T] =
    cond(options.contains(value), value, s"${name}が適切な値（${options}）でありません。")

  def inRange(value: Int, min: Int, max: Int): Result[Int] =
    cond(
      value >= min && value <= max,
      value,
      s"${name}が適切な範囲内（${min}以上${max}以下）でありません。"
    )

  def consistentValidRange[T](
      validFrom: LocalDate,
      validUpto: Option[LocalDate],
      validValue: T,
      errMessage: => String
  ): Result[T] =
    cond(
      validUpto.isEmpty || validFrom <= validUpto.get,
      validValue,
      errMessage
    )

  def consistentValidRange[T](
      validFrom: LocalDate,
      validUpto: Option[LocalDate],
      validValue: T
  ): Result[T] =
    cond(
      validUpto.isEmpty || validFrom <= validUpto.get,
      validValue,
      "有効期限の開始日が終了日よりも後です。"
    )

class DatabaseIdValidator[E](err: E, name: String)
    extends SectionValidator(err, name):
  def validateForEnter: Result[Int] = valid(0)

  def validateForUpdate(value: Int): Result[Int] =
    positive(value)

  def validateOptionForUpdate(value: Option[Int]): Result[Int] =
    some(value)
      |> validateForUpdate

class ValidFromValidator[E](err: E, name: String = "期限開始")
    extends SectionValidator(err, name):
  import Implicits.*
  def validate(date: LocalDate): Result[LocalDate] =
    valid(date)

  def validateOption(dateOption: Option[LocalDate]): Result[LocalDate] =
    some(dateOption) |> validate

class ValidUptoValidator[E, T](
    err: E,
    f: Option[LocalDate] => T,
    name: String = "期限終了"
) extends SectionValidator(err, name):
  def validate(dateOption: Option[LocalDate]): Result[T] =
    valid(f(dateOption))

class GlobalValidator[E, T](
    err: E,
    f: T => Boolean,
    errMessage: () => String
):
  def validate(t: T): ValidatedResult[E, T] =
    if f(t) then Valid(t)
    else Invalid(List((err, errMessage())))

class ConsistentValidRangeValidator[E, T](
    err: E,
    validFrom: T => LocalDate,
    validUpto: T => Option[LocalDate],
    errMessage: () => String = () => "有効期限開始が有効期限終了の後です。"
) extends GlobalValidator[E, T](
      err,
      t => validUpto(t) == None || validFrom(t) <= validUpto(t).get,
      errMessage
    )
