package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.Patient
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
import dev.myclinic.scala.model.Sex

object SexValidator:
  sealed trait SexError:
    def message: String

  object InvalidSexError extends SexError:
    def message: String = "性別の入力が不適切です。"

  def validateSexInput(input: Option[String]): Result[Sex] =
    nonNullOpt(input, InvalidSexError)
      .andThen(input =>
        input match {
          case "M" | "男" => validNec(Sex.Male)
          case "F" | "女" => validNec(Sex.Female)
          case _         => invalidNec(InvalidSexError)
        }
      )

  type Result[T] = ValidatedNec[SexError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] = Validators.toEither(r, _.message)

  def validateSex(
      sexResult: Result[Sex]
  ) = sexResult
