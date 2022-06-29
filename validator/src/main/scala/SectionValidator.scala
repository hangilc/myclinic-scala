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

type ValidatedSection[E, T] = Validated[List[(E, String)], T]

extension [E, T](v: ValidatedSection[E, T])
  def asEither: Either[String, T] =
    v.toEither.left.map(_.map(_._2).mkString("\n"))

extension [E, T](v: ValidatedSection[E, T])
  def |>[U](f: T => ValidatedSection[E, U]): ValidatedSection[E, U] =
    v.andThen(f)

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


