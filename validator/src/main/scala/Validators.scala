package dev.myclinic.scala.validator

import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import dev.myclinic.scala.util.DateTimeOrdering.{given}
import scala.math.Ordered.orderingToOrdered
import java.time.{LocalTime}
import java.util.regex.Pattern
import scala.util.matching.Regex

object Validators:
  def nonEmpty[E](input: String, err: => E): ValidatedNec[E, String] =
    condNec(!input.isEmpty, input, err)

  def isInt[E](input: String, err: => E): ValidatedNec[E, Int] =
    try validNec(input.toInt)
    catch {
      case _: Throwable => invalidNec(err)
    }

  def nonNegativeInt[E](input: Int, err: => E): ValidatedNec[E, Int] =
    condNec(input >= 0, input, err)

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

