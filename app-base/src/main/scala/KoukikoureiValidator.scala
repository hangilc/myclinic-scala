package dev.myclinic.scala.web.appbase.validator

import cats.syntax.all.*
import cats.data.Validated
import dev.fujiwara.validator.section.*
import dev.fujiwara.validator.section.Implicits.*
import dev.myclinic.scala.model.ValidUpto
import java.time.LocalDate
import dev.myclinic.scala.model.Koukikourei

object KoukikoureiValidator:

  sealed trait KoukikoureiError

  object KoukikoureiIdError extends KoukikoureiError
  object PatientIdError extends KoukikoureiError
  object HokenshaBangouError extends KoukikoureiError
  object HihokenshaBangouError extends KoukikoureiError
  object FutanWariError extends KoukikoureiError
  object ValidFromError extends KoukikoureiError
  object ValidUptoError extends KoukikoureiError
  object InconsistentValidRangeError extends KoukikoureiError

  type SectionResult[E, T] = ValidatedSection[E, T]

  extension [E <: KoukikoureiError, T] (r: SectionResult[E, T])
    def asKoukikoureiError: SectionResult[KoukikoureiError, T] = r

  object KoukikoureiIdValidator
      extends DatabaseIdValidator(KoukikoureiIdError, "koukikourei-id")

  object PatientIdValidator
      extends SectionValidator(PatientIdError, "patient-id"):
    def validate(value: Int): Result[Int] =
      positive(value)

  object HokenshaBangouValidator
      extends SectionValidator(HokenshaBangouError, "保険者番号"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object HihokenshaBangouValidator
      extends SectionValidator(HihokenshaBangouError, "被保険者番号"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object FutanWariValidator extends SectionValidator(FutanWariError, "負担割"):
    def validate(value: Int): Result[Int] =
      inRange(value, 1, 3)

  object ValidFromValidator extends ValidFromValidator(ValidFromError)

  object ValidUptoValidator
      extends ValidUptoValidator(ValidUptoError, ValidUpto.apply _)

  object ConsistentValidRangeValidator
      extends ConsistentValidRangeValidator[KoukikoureiError, Koukikourei](
        InconsistentValidRangeError,
        _.validFrom, _.validUpto.value
      )

  def validate(
      koukikoureiIdResult: SectionResult[KoukikoureiIdError.type, Int],
      patientIdResult: SectionResult[PatientIdError.type, Int],
      hokenshaBangouResult: SectionResult[HokenshaBangouError.type, String],
      hihokenshaBangouResult: SectionResult[HihokenshaBangouError.type, String],
      futanWariResult: SectionResult[FutanWariError.type, Int],
      validFromResult: SectionResult[ValidFromError.type, LocalDate],
      validUptoResult: SectionResult[ValidUptoError.type, ValidUpto]
  ): ValidatedResult[KoukikoureiError, Koukikourei] =
    (
      koukikoureiIdResult.asKoukikoureiError,
      patientIdResult.asKoukikoureiError,
      hokenshaBangouResult.asKoukikoureiError,
      hihokenshaBangouResult.asKoukikoureiError,
      futanWariResult.asKoukikoureiError,
      validFromResult.asKoukikoureiError,
      validUptoResult.asKoukikoureiError,
    ).mapN(Koukikourei.apply _) |> ConsistentValidRangeValidator.validate

  export Implicits.asEither

