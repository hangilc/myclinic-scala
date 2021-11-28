package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.{Patient, Sex}
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
import dev.myclinic.scala.validator.{SexValidator, DateValidator}
import java.time.LocalDate

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
  case class SexError(error: SexValidator.SexError) extends PatientError:
    def message: String = error.message
  case class BirthdayError(error: DateValidator.DateError) extends PatientError:
    def message: String = error.message

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
