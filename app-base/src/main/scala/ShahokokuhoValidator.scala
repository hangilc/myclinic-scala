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
import dev.fujiwara.validator.section.Implicits.*

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
  object InconsistentValidRangeError extends ShahokokuhoError

  object ShahokokuhoIdValidator
      extends SectionValidator(ShahokokuhoIdError, "shahokokuho-id"):
    def validateForEnter: Result[Int] = valid(0)

    def validateForUpdate(value: Int): Result[Int] =
      positive(value)

    def validateOptionForUpdate(value: Option[Int]): Result[Int] =
      some(value)
        |> validateForUpdate

  object PatientIdValidator
      extends SectionValidator(PatientIdError, "patient-id"):
    def validate(patientId: Int): Result[Int] =
      positive(patientId)

    def validateOption(patientIdOption: Option[Int]) =
      some(patientIdOption)
        |> validate

  object HokenshaBangouValidator
      extends SectionValidator(HokenshaBangouError, "保険者番号"):
    def validate(value: Int): Result[Int] =
      positive(value)

    def validateInput(input: String): Result[Int] =
      inputToInt(input)
        |> validate

  object HihokenshaKigouValidator
      extends SectionValidator(HihokenshaKigouError, "被保険者記号"):
    def validate(input: String): Result[String] =
      notNull(input)

  object HihokenshaBangouValidator
      extends SectionValidator(HihokenshaBangouError, "被保険者番号"):
    def validate(input: String): Result[String] =
      notNull(input)

  object HonninValidator extends SectionValidator(HonninError, "本人・家族"):
    def validate(value: Int): Result[Int] =
      oneOf(value, List(0, 1))

    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> validate

    def validateInputOption(inputOption: Option[String]): Result[Int] =
      some(inputOption) |> validateInput

  object ValidFromValidator extends SectionValidator(ValidFromError, "期限開始"):
    def validate(date: LocalDate): Result[LocalDate] =
      valid(date)

    def validateOption(dateOption: Option[LocalDate]): Result[LocalDate] =
      some(dateOption) |> validate

  object ValidUptoValidator extends SectionValidator(ValidUptoError, "期限終了"):
    def validate(dateOption: Option[LocalDate]): Result[ValidUpto] =
      valid(ValidUpto(dateOption))

  object KoureiValidator extends SectionValidator(KoureiError, "高齢"):
    def validate(value: Int): Result[Int] =
      oneOf(value, List(0, 2, 3))

    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> validate

  object EdabanValidator extends SectionValidator(KoureiError, "枝番"):
    def validate(src: String): Result[String] =
      notNull(src)

  object ConsistentHihokenshaValidator
      extends SectionValidator(InconsistentHihokenshaError, ""):
    def validate(h: Shahokokuho): Result[Shahokokuho] =
      condNot(
        h.hihokenshaKigou.isEmpty && h.hihokenshaBangou.isEmpty,
        h,
        "被保険者の記号と番号が両方空白です。"
      )

  object ConsistentValidRangeValidator
      extends SectionValidator(InconsistentValidRangeError, ""):
    def validate(h: Shahokokuho): Result[Shahokokuho] =
      consistentValidRange(h.validFrom, h.validUpto.value, h)

  type ShahokokuhoResult[T] = ValidatedSection[ShahokokuhoError, T]

  def validate(
      shahokokuhoIdResult: ShahokokuhoIdValidator.type#Result[Int],
      patientIdResult: PatientIdValidator.type#Result[Int],
      hokenshaBangouResult: HokenshaBangouValidator.type#Result[Int],
      hihokenshaKigouResult: HihokenshaKigouValidator.type#Result[String],
      hihokenshaBangouResult: HihokenshaBangouValidator.type#Result[String],
      honninResult: HonninValidator.type#Result[Int],
      validFromResult: ValidFromValidator.type#Result[LocalDate],
      validUptoResult: ValidUptoValidator.type#Result[ValidUpto],
      koureiResult: KoureiValidator.type#Result[Int],
      edabanResult: EdabanValidator.type#Result[String]
  ): ValidatedSection[ShahokokuhoError, Shahokokuho] =
    val gShahokokuhoIdResult: ShahokokuhoResult[Int] =
      shahokokuhoIdResult
    val gPatientIdResult: ShahokokuhoResult[Int] =
      patientIdResult
    val gHokenshaBangouResult: ShahokokuhoResult[Int] =
      hokenshaBangouResult
    val gHihokenshaKigouResult: ShahokokuhoResult[String] =
      hihokenshaKigouResult
    val gHihokenshaBangouResult: ShahokokuhoResult[String] =
      hihokenshaBangouResult
    val gHonninResult: ShahokokuhoResult[Int] = honninResult
    val gValidFromResult: ShahokokuhoResult[LocalDate] =
      validFromResult
    val gValidUptoResult: ShahokokuhoResult[ValidUpto] =
      validUptoResult
    val gKoureiResult: ShahokokuhoResult[Int] = koureiResult
    val gEdabanResult: ShahokokuhoResult[String] = edabanResult
    (
      gShahokokuhoIdResult,
      gPatientIdResult,
      gHokenshaBangouResult,
      gHihokenshaKigouResult,
      gHihokenshaBangouResult,
      gHonninResult,
      gValidFromResult,
      gValidUptoResult,
      gKoureiResult,
      gEdabanResult
    ).mapN(Shahokokuho.apply _)
      |> ConsistentHihokenshaValidator.validate
      |> ConsistentValidRangeValidator.validate

  export Implicits.asEither
