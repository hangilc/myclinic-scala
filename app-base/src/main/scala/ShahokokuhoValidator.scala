package dev.myclinic.scala.web.appbase

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
  object EdabanError extends ShahokokuhoError
  object InconsistentHihokenshaError extends ShahokokuhoError
  object InconsistentValidRangeError extends ShahokokuhoError

  object ShahokokuhoIdValidator
      extends DatabaseIdValidator(ShahokokuhoIdError, "shahokokuho-id")

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
    def validate(value: ValidUpto): Result[ValidUpto] =
      valid(value)

    def validateDateOption(dateOption: Option[LocalDate]): Result[ValidUpto] =
      validate(ValidUpto(dateOption))

  val validKoureiValues: List[Int] = List(0, 1, 2, 3)
  
  object KoureiValidator extends SectionValidator(KoureiError, "高齢"):
    def validate(value: Int): Result[Int] =
      oneOf(value, validKoureiValues)

    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> validate

  object EdabanValidator extends SectionValidator(EdabanError, "枝番"):
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

  def validate(
      rs: (
          ValidatedSection[ShahokokuhoIdError.type, Int],
          ValidatedSection[PatientIdError.type, Int],
          ValidatedSection[HokenshaBangouError.type, Int],
          ValidatedSection[HihokenshaKigouError.type, String],
          ValidatedSection[HihokenshaBangouError.type, String],
          ValidatedSection[HonninError.type, Int],
          ValidatedSection[ValidFromError.type, LocalDate],
          ValidatedSection[ValidUptoError.type, ValidUpto],
          ValidatedSection[KoureiError.type, Int],
          ValidatedSection[EdabanError.type, String]
      )
  ): ValidatedSection[ShahokokuhoError, Shahokokuho] =
    val dummy: ValidatedSection[ShahokokuhoError, Int] = Valid(1)
    (dummy *: rs).tupled.map(args => Shahokokuho.apply.tupled(args.tail))
      |> ConsistentHihokenshaValidator.validate
      |> ConsistentValidRangeValidator.validate

