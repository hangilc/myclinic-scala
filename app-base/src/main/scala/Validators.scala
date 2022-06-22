package dev.myclinic.scala.web.appbase

import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import scala.math.Ordered.orderingToOrdered
import java.time.{LocalTime}
import java.util.regex.Pattern
import scala.util.matching.Regex
import cats.instances.try_

object Validators:
  def nonNull[E, T <: AnyRef](input: T, err: => E): ValidatedNec[E, T] =
    condNec(input != null, input, err)

  def nonNullOpt[E, T <: AnyRef](input: Option[T], err: => E): ValidatedNec[E, T] =
    input match {
      case None => invalidNec(err)
      case Some(t) => condNec(t != null, t, err)
    }

  def nonEmpty[E](input: String, err: => E): ValidatedNec[E, String] =
    condNec(input != null && !input.isEmpty, input, err)

  def isInt[E](input: String, err: => E): ValidatedNec[E, Int] =
    try validNec(input.toInt)
    catch {
      case _: Throwable => invalidNec(err)
    }

  def nonNegativeInt[E](input: Int, err: => E): ValidatedNec[E, Int] =
    condNec(input >= 0, input, err)

  def positiveInt[E](input: Int, err: => E): ValidatedNec[E, Int] =
    condNec(input > 0, input, err)

  def isLocalTime[E](input: String, err: => E): ValidatedNec[E, LocalTime] =
    try
      validNec(LocalTime.parse(input))
    catch {
      case _: Exception => invalidNec(err)
    }

  def timeIsBeforeOrEqual[E, A](
      a: LocalTime,
      b: LocalTime,
      v: A,
      err: => E
  ): ValidatedNec[E, A] =
    condNec(a <= b, v, err)

  def regexMatch[E](input: String, regex: Regex, err: => E): ValidatedNec[E, String] =
    if regex.matches(input) then validNec(input)
    else invalidNec(err)

  def errorMessage[E](errs: NonEmptyChain[E], msg: E => String): String =
    errs.toNonEmptyList.toList.map(msg(_)).mkString("\n")

  def toEither[E, A](v: ValidatedNec[E, A], msg: E => String): Either[String, A] =
    v match {
      case Valid(a) => Right(a)
      case Invalid(e) => Left(errorMessage(e, msg))
    }

