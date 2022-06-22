package dev.fujiwara.validator

import cats.data.Validated
import cats.data.Validated.*
import java.time.LocalDate
import math.Ordering.Implicits.infixOrderingOps

object ValidatorUtil:
  def isPositive[E](value: Int, e: E): Validated[List[E], Int] =
    if value > 0 then Valid(value) else Invalid(List(e))

  def isNotEmpty[E](value: String, e: E): Validated[List[E], String] =
    if value == null || value.isEmpty then Invalid(List(e))
    else Valid(value)

  def isSome[E, T](option: Option[T], e: E): Validated[List[E], T] =
    option match {
      case Some(t) => Valid(t)
      case None    => Invalid(List(e))
    }

  def toInt[E](src: String, e: E): Validated[List[E], Int] =
    src.toIntOption match {
      case Some(i) => Valid(i)
      case None    => Invalid(List(e))
    }

  def condValid[E, T](cond: Boolean, valid: T, err: E): Validated[List[E], T] =
    if cond then Valid(valid) else Invalid(List(err))

  def isOneOf[E, T](value: T, options: List[T], err: E): Validated[List[E], T] =
    condValid(options.contains(value), value, err)

  def nonNullString(s: String): Valid[String] =
    Valid(if s == null then "" else s)

  def isConsistentDateRange[E, T](
      validFrom: LocalDate,
      validUpto: Option[LocalDate],
      value: T,
      err: E
  ): Validated[List[E], T] =
    validUpto match {
      case None    => Valid(value)
      case Some(d) => condValid(validFrom <= d, value, err)
    }

  trait ValidationError:
    def message: String

  extension [E <: ValidationError, T](validated: Validated[List[E], T])
    def asEither: Either[String, T] =
      validated.toEither.left.map(_.map(_.message).mkString("\n"))

  object ErrorMessages:
    def isEmptyErrorMessage(name: String): String = s"${name}が入力されていません。"
    def notIntegerErrorMessage(name: String): String = s"${name}の入力が整数でありません。"
    def notPositiveErrorMessage(name: String): String = s"${name}の値が正の整数でありません。"
    def invalidValueErrorMessage(name: String): String = s"${name}の値が適切でありません。"
