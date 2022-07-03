package dev.myclinic.scala.web.appbase

import cats.syntax.all.*
import cats.data.Validated
import cats.data.Validated.Valid
import dev.fujiwara.validator.section.*
import dev.fujiwara.validator.section.Implicits.*
import dev.myclinic.scala.model.ValidUpto
import java.time.LocalDate
import dev.myclinic.scala.model.Roujin

object RoujinValidator:

  sealed trait RoujinError

  object RoujinIdError extends RoujinError
  object PatientIdError extends RoujinError
  object ShichousonError extends RoujinError
  object JukyuushaError extends RoujinError
  object FutanWariError extends RoujinError
  object ValidFromError extends RoujinError
  object ValidUptoError extends RoujinError
  object InconsistentValidRangeError extends RoujinError

  type SectionResult[E, T] = ValidatedSection[E, T]

  extension [E <: RoujinError, T](r: SectionResult[E, T])
    def asRoujinError: SectionResult[RoujinError, T] = r

  object RoujinIdValidator
      extends DatabaseIdValidator(RoujinIdError, "roujin-id")

  object PatientIdValidator
      extends SectionValidator(PatientIdError, "patient-id"):
    def validate(value: Int): Result[Int] =
      positive(value)

    def validateOption(value: Option[Int]): Result[Int] =
      some(value) |> validate

  object ShichousonValidator
      extends SectionValidator(ShichousonError, "市町村番号"):
    def validate(value: Int): Result[Int] =
      positive(value)

    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> validate

  object JukyuushaValidator
      extends SectionValidator(JukyuushaError, "受給者番号"):
    def validate(value: Int): Result[Int] =
      positive(value)

    def validateInput(input: String): Result[Int] =
      inputToInt(input) |> validate

  object FutanWariValidator extends SectionValidator(FutanWariError, "負担割"):
    def validate(value: Int): Result[Int] =
      inRange(value, 1, 3)

  object ValidFromValidator extends ValidFromValidator(ValidFromError)

  object ValidUptoValidator
      extends ValidUptoValidator(ValidUptoError, ValidUpto.apply _)

  object ConsistentValidRangeValidator
      extends ConsistentValidRangeValidator[RoujinError, Roujin](
        InconsistentValidRangeError,
        _.validFrom,
        _.validUpto.value
      )

  def validate(
      rs: (
          SectionResult[RoujinIdError.type, Int],
          SectionResult[PatientIdError.type, Int],
          SectionResult[ShichousonError.type, Int],
          SectionResult[JukyuushaError.type, Int],
          SectionResult[FutanWariError.type, Int],
          SectionResult[ValidFromError.type, LocalDate],
          SectionResult[ValidUptoError.type, ValidUpto]
      )
  ): ValidatedResult[RoujinError, Roujin] =
    val dummy: ValidatedResult[RoujinError, Int] = Valid(0)
    (dummy *: rs).tupled.map(_.tail).map(Roujin.apply.tupled)
      |> ConsistentValidRangeValidator.validate

  export Implicits.asEither
