package dev.myclinic.scala.web.appbase.validator

import cats.syntax.all.*
import cats.data.Validated
import cats.data.Validated.*
import dev.fujiwara.validator.section.*
import dev.fujiwara.validator.section.Implicits.*
import dev.myclinic.scala.model.ValidUpto
import java.time.LocalDate
import dev.myclinic.scala.model.Kouhi

object KouhiValidator:

  sealed trait KouhiError:
    def asKouhiError: KouhiError = KouhiError
  object KouhiError extends KouhiError

  object KouhiIdError extends KouhiError
  object FutanshaError extends KouhiError
  object JukyuushaError extends KouhiError
  object ValidFromError extends KouhiError
  object ValidUptoError extends KouhiError
  object PatientIdError extends KouhiError
  object InconsistentValidRangeError extends KouhiError

  // extension [E <: KouhiError, T](r: ValidatedResult[E, T])
  //   def asKouhiError: ValidatedResult[KouhiError, T] = r

  def asKouhiError[E, T](
      r: ValidatedResult[E, T]
  ): ValidatedResult[KouhiError, T] =
    r match {
      case Invalid(e) => Invalid(e.map { (e, s) => (KouhiError, s)})
      case Valid(t)   => Valid(t)
    }

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

  case class ValidationResults(
      kouhidIdResult: ValidatedResult[KouhiIdError.type, Int],
      futanshaResult: ValidatedResult[FutanshaError.type, Int],
      jukyuushaResult: ValidatedResult[JukyuushaError.type, Int],
      validFromResult: ValidatedResult[ValidFromError.type, LocalDate],
      validUptoResult: ValidatedResult[ValidUptoError.type, ValidUpto],
      patientIdResult: ValidatedResult[PatientIdError.type, Int]
  )

  type CastToResult[A] = A match {
    case ValidatedResult[e, t] => ValidatedResult[KouhiError, t]
  }

  def castToResult[A, E <: KouhiError](a: A): CastToResult[A] = a match {
    case r: ValidatedResult[E, t] => asKouhiError(r)
  }

  def validateForEnter(
      results: ValidationResults
  ): ValidatedResult[KouhiError, Kouhi] =
    val rs = Tuple
      .fromProductTyped(results)
      .map[[A] =>> ValidatedResult[KouhiError, A]](
        [T] => (t: T) => castToResult(t)
      )
    ???
// .mapN(Kouhi.apply _)

// |> ConsistentValidRangeValidator.validate
