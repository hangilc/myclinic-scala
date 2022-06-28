package dev.myclinic.scala.web.appbase.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Shahokokuho, ValidUpto}
import java.time.LocalDate
import cats.data.Validated
import cats.data.Validated.*
import scala.util.{Try, Success, Failure}
import dev.fujiwara.validator.section.*

object ShahokokuhoValidator:

  sealed trait ShahokokuhoError
  object ShahokokuhoIdError extends ShahokokuhoError
  object PatientIdError extends ShahokokuhoError
  object HokenshaBangouError extends ShahokokuhoError
  object HihokenshaKigouError extends ShahokokuhoError
  object HihokenshaBangouError extends ShahokokuhoError
  object HonninError extends ShahokokuhoError
  object ValidFromError extends ShahokokuhoError
  object ValidUptoError extends ShahokokuhoError
  object KoureiError extends ShahokokuhoError
  object InconsistentHihokenshaError extends ShahokokuhoError

  object ShahokokuhoIdValidator extends SectionValidator(ShahokokuhoIdError, "shahokokuho-id"):
    def validateForEnter: Result[Int] = valid(0)

    def validateForUpdate(value: Int): Result[Int] =
      positive(value)

    def validateOptionForUpdate(value: Option[Int]): Result[Int] =
      some(value) 
        |> validateForUpdate
  
  object PatientIdValidator extends SectionValidator(PatientIdError, "patient-id"):
    def validate(patientId: Int): Result[Int] =
      positive(patientId)

    def validateOption(patientIdOption: Option[Int]) = 
      some(patientIdOption)
        |> validate

  object HokenshaBangouValidator extends SectionValidator(HokenshaBangouError, "保険者番号"):
    def validate(value: Int): Result[Int] =
      positive(value)

    def validateInput(input: String): Result[Int] =
      inputToInt(input)
        |> validate

  object HihokenshaKigouValidator extends SectionValidator(HihokenshaKigouError, "被保険者記号"):
    def validate(input: String): Result[String] =
      notNull(input)

  object HihokenshaBangouValidator extends SectionValidator(HihokenshaBangouError, "被保険者記号"):
    def validate(input: String): Result[String] =
      notNull(input)

  
  def validateHonnin(value: Int): Result[Int] =
    isOneOf(value, List(0, 1), HonninError.invalid)

  def validateHonninInput(srcOpt: Option[String]): Result[Int] =
    isSome(srcOpt, HonninError.empty)
      .andThen(toInt(_, HonninError.notInteger))
      .andThen(validateHonnin(_))

  def validateValidFrom(dateOption: Option[LocalDate]): Result[LocalDate] =
    isSome(dateOption, ValidFromError.empty)

  def validateValidUpto(dateOption: Option[LocalDate]): Result[ValidUpto] = 
    Valid(ValidUpto(dateOption))

  def validateKourei(value: Int): Result[Int] =
    isOneOf(value, List(0, 2, 3), KoureiError.invalid)

  def validateKoureiInput(srcOpt: Option[String]): Result[Int] =
    isSome(srcOpt, KoureiError.empty)
      .andThen(toInt(_, KoureiError.notInteger))
      .andThen(validateKourei(_))

  def validateEdaban(src: String): Result[String] = nonNullString(src)

  def validateShahokokuho(
      shahokokuhoIdResult: Result[Int],
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[Int],
      hihokenshaKigouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      honninResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto],
      koureiResult: Result[Int],
      edabanResult: Result[String]
  ): Result[Shahokokuho] =
    (
      shahokokuhoIdResult,
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaKigouResult,
      hihokenshaBangouResult,
      honninResult,
      validFromResult,
      validUptoResult,
      koureiResult,
      edabanResult
    ).mapN(Shahokokuho.apply)
      .andThen(shaho =>
        if shaho.hihokenshaKigou.isEmpty && shaho.hihokenshaBangou.isEmpty then
          Invalid(List(InconsistentHihokenshaError("被保険者記号・番号が両方空白です。")))
        else Valid(shaho)
      )

  def validateShahokokuhoForEnter(
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[Int],
      hihokenshaKigouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      honninResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto],
      koureiResult: Result[Int],
      edabanResult: Result[String]
  ): Result[Shahokokuho] =
    validateShahokokuho(
      Valid(0),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaKigouResult,
      hihokenshaBangouResult,
      honninResult,
      validFromResult,
      validUptoResult,
      koureiResult,
      edabanResult
    )
