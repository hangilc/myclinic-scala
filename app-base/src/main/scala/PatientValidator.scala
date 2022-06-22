package dev.myclinic.scala.web.appbase

import cats.*
import cats.syntax.*
import cats.data.Validated
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Patient, Sex}
import Validators.*
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import scala.quoted.Type
import dev.fujiwara.validator.ValidatorUtil.*

object PatientValidator:
  sealed trait PatientError extends ValidationError

  object NonZeroPatientIdError extends PatientError:
    def message: String = "Non-zero patient-id"
  object ZeroPatientIdError extends PatientError:
    def message: String = "Zero patient-id"
  object EmptyFirstNameError extends PatientError:
    def message: String = "姓が入力されていません。"
  object EmptyLastNameError extends PatientError:
    def message: String = "名が入力されていません。"
  object EmptyFirstNameYomiError extends PatientError:
    def message: String = "姓のよみが入力されていません。"
  object EmptyLastNameYomiError extends PatientError:
    def message: String = "名のよみが入力されていません。"
  case class SexError(sexError: List[SexValidator.SexError]) extends PatientError:
    def message: String = sexError(0).message
  object EmptyBirthday extends PatientError:
    def message: String = "生年月日が入力されていません。"

  type Result[T] = Validated[List[PatientError], T]

  def validatePatientIdForUpdate(patientId: Int): Result[Int] =
    condValid(patientId != 0, patientId, ZeroPatientIdError)
  def validateLastName(input: String): Result[String] =
    isNotEmpty(input, EmptyLastNameError)
  def validateFirstName(input: String): Result[String] =
    isNotEmpty(input, EmptyFirstNameError)
  def validateLastNameYomi(input: String): Result[String] =
    isNotEmpty(input, EmptyLastNameYomiError)
  def validateFirstNameYomi(input: String): Result[String] =
    isNotEmpty(input, EmptyFirstNameYomiError)
  def validateSexInput(input: Option[String]): Result[Sex] =
    SexValidator.validateSexInput(input).leftMap(err => List(SexError(err)))
  def validateSex(sex: Sex): Result[Sex] = Valid(sex)
  def validateBirthday(dateOption: Option[LocalDate]): Result[LocalDate] =
    isSome(dateOption, EmptyBirthday)
  def validateAddress(input: String): Result[String] = Valid(input)
  def validatePhone(input: String): Result[String] = Valid(input)

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
      Valid(0),
      lastNameResult,
      firstNameResult,
      lastNameYomiResult,
      firstNameYomiResult,
      sexResult,
      birthdayResult,
      addressResult,
      phoneResult
    )
      .mapN(Patient.apply _)

  def validatePatientForUpdate(
      patientIdResult: Result[Int],
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
      patientIdResult,
      lastNameResult,
      firstNameResult,
      lastNameYomiResult,
      firstNameYomiResult,
      sexResult,
      birthdayResult,
      addressResult,
      phoneResult
    )
      .mapN(Patient.apply _)
