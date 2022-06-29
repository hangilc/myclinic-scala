package dev.myclinic.scala.web.appbase.validator

import cats.syntax.all.*
import cats.data.Validated
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

  extension [E <: KouhiError, T](r: ValidatedResult[E, T])
    def asKouhiError: ValidatedResult[KouhiError, T] = r

  object KouhiIdValidator extends DatabaseIdValidator(KouhiIdError, "kouhi-id")

  object FutanshaValidator extends SectionValidator(FutanshaError, "負担者番号"):
    def validate(input: String): Result[Int] =
      inputToInt(input) |> positive

  object JukyuushaValidator extends SectionValidator(JukyuushaError, "受給者番号"):
    def validate(input: String): Result[Int] =
      inputToInt(input) |> positive

  object ValidFromValidator extends ValidFromValidator(ValidFromError)

  object ValidUptoValidator
      extends ValidUptoValidator(ValidUptoError, ValidUpto.apply _)

  object PatientIdValidator
      extends SectionValidator(PatientIdError, "patient-id"):
    def validate(value: Int): Result[Int] =
      positive(value)

  object ConsistentValidRangeValidator
      extends ConsistentValidRangeValidator[KouhiError, Kouhi](
        InconsistentValidRangeError,
        _.validFrom,
        _.validUpto.value
      )

  def validateForEnter(
      kouhidIdResult: ValidatedResult[KouhiIdError.type, Int],
      futanshaResult: ValidatedResult[FutanshaError.type, Int],
      jukyuushaResult: ValidatedResult[JukyuushaError.type, Int],
      validFromResult: ValidatedResult[ValidFromError.type, LocalDate],
      validUptoResult: ValidatedResult[ValidUptoError.type, ValidUpto],
      patientIdResult: ValidatedResult[PatientIdError.type, Int]
  ): ValidatedResult[KouhiError, Kouhi] =
    (
      kouhidIdResult.asKouhiError,
      futanshaResult.asKouhiError,
      jukyuushaResult.asKouhiError,
      validFromResult.asKouhiError,
      validUptoResult.asKouhiError,
      patientIdResult.asKouhiError,
    ).mapN(Kouhi.apply _) |> ConsistentValidRangeValidator.validate
    
