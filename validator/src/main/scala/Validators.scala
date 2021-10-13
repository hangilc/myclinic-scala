package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.data.Validated.*
import dev.myclinic.scala.util.DateTimeOrdering.{given}
import scala.math.Ordered.orderingToOrdered
import java.time.{LocalTime}

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

  def timeIsBeforeOrEqual[E](
      a: LocalTime,
      b: LocalTime,
      err: => E
  ): ValidatedNec[E, Unit] =
    condNec(a <= b, (), err)
