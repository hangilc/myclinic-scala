package dev.myclinic.scala.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Patient, Sex}
import dev.myclinic.scala.validator.Validators.*
import dev.myclinic.scala.validator.SexValidator
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import scala.quoted.Type

object PatientValidator:
  sealed trait PatientError:
    def message: String

  object NonZeroPatientIdError extends PatientError:
    def message: String = "Non-zero patient-id"
  object ZeroPatientIdError extends PatientError:
    def message: String = "Non-zero patient-id"
  object EmptyFirstNameError extends PatientError:
    def message: String = "姓が入力されていません。"
  object EmptyLastNameError extends PatientError:
    def message: String = "名が入力されていません。"
  object EmptyFirstNameYomiError extends PatientError:
    def message: String = "姓のよみが入力されていません。"
  object EmptyLastNameYomiError extends PatientError:
    def message: String = "名のよみが入力されていません。"
  case class SexError(error: NonEmptyChain[SexValidator.SexError])
      extends PatientError:
    def message: String = error.toList.map(_.message).mkString("\n")
  case class BirthdayError[E](error: NonEmptyChain[E], messageOf: E => String)
      extends PatientError:
    def message: String = error.toList.map("（生年月日）" + messageOf(_)).mkString("\n")

  type Result[T] = ValidatedNec[PatientError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] = Validators.toEither(r, _.message)

  def validateLastName(input: String): Result[String] =
    nonEmpty(input, EmptyLastNameError)
  def validateFirstName(input: String): Result[String] =
    nonEmpty(input, EmptyFirstNameError)
  def validateLastNameYomi(input: String): Result[String] =
    nonEmpty(input, EmptyLastNameYomiError)
  def validateFirstNameYomi(input: String): Result[String] =
    nonEmpty(input, EmptyFirstNameYomiError)
  def validateSex(result: SexValidator.Result[Sex]): Result[Sex] =
    result match {
      case Valid(sex)   => validNec(sex)
      case Invalid(err) => invalidNec(SexError(err))
    }
  def validateBirthday[E](
      result: ValidatedNec[E, LocalDate],
      messageOf: E => String
  ): Result[LocalDate] =
    result match {
      case Valid(sex)   => validNec(sex)
      case Invalid(err) => invalidNec(BirthdayError(err, messageOf))
    }
  // def validateBirthday(result: DateValidator.Result[LocalDate]): Result[LocalDate] =
  //   result match {
  //     case Valid(sex) => validNec(sex)
  //     case Invalid(err) => invalidNec(BirthdayError(err))
  //   }
  def validateAddress(input: String): Result[String] =
    validNec(if input == null then "" else input)
  def validatePhone(input: String): Result[String] =
    validNec(if input == null then "" else input)

  def validatePatientForEnter(
      lastNameResult: Result[String],
      firstNameResult: Result[String],
      lastNameYomiResult: Result[String],
      firstNameYomiResult: Result[String],
      sexResult: Result[Sex],
      birthdayResult: Result[LocalDate],
      addressResult: Result[String],
      phoneResult: Result[String]
  ): Result[Patient] =
    (
      validNec(0),
      lastNameResult,
      firstNameResult,
      lastNameYomiResult,
      firstNameYomiResult,
      sexResult,
      birthdayResult,
      addressResult,
      phoneResult
    )
      .mapN(Patient.apply)
