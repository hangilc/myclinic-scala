package dev.myclinic.scala.validator

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
