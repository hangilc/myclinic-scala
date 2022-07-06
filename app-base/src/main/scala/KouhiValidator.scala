package dev.myclinic.scala.web.appbase

import cats.syntax.all.*
import cats.data.Validated
import cats.data.Validated.*
import dev.fujiwara.validator.section.*
import dev.fujiwara.validator.section.Implicits.*
import dev.myclinic.scala.model.ValidUpto
import java.time.LocalDate
import dev.myclinic.scala.model.Kouhi

object KouhiValidator:

  sealed trait KouhiError

  object KouhiIdError extends KouhiError
  object FutanshaError extends KouhiError
  object JukyuushaError extends KouhiError
  object ValidFromError extends KouhiError
  object ValidUptoError extends KouhiError
  object PatientIdError extends KouhiError
  object InconsistentValidRangeError extends KouhiError

  object KouhiIdValidator extends DatabaseIdValidator(KouhiIdError, "kouhi-id")

  object FutanshaValidator extends SectionValidator(FutanshaError, "負担者番号"):
    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> positive

  object JukyuushaValidator extends SectionValidator(JukyuushaError, "受給者番号"):
    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> positive

  object ValidFromValidator extends ValidFromValidator(ValidFromError)

  object ValidUptoValidator
      extends ValidUptoValidator(ValidUptoError, ValidUpto.apply _)

  object PatientIdValidator
      extends SectionValidator(PatientIdError, "patient-id"):
    def validate(value: Int): Result[Int] =
      positive(value)

    def validateOption(value: Option[Int]): Result[Int] =
      some(value) |> validate

  object ConsistentValidRangeValidator
      extends ConsistentValidRangeValidator[KouhiError, Kouhi](
        InconsistentValidRangeError,
        _.validFrom,
        _.validUpto.value
      )

  def validate(
      rs: (
          ValidatedResult[KouhiIdError.type, Int],
          ValidatedResult[FutanshaError.type, Int],
          ValidatedResult[JukyuushaError.type, Int],
          ValidatedResult[ValidFromError.type, LocalDate],
          ValidatedResult[ValidUptoError.type, ValidUpto],
          ValidatedResult[PatientIdError.type, Int]
      )
  ): ValidatedResult[KouhiError, Kouhi] =
    val dm: ValidatedResult[KouhiError, Int] = Valid(0)
    (dm *: rs).tupled
      .map(_.tail)
      .map(Kouhi.apply.tupled)
      |> ConsistentValidRangeValidator.validate
