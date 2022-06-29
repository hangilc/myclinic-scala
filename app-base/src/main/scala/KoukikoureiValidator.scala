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

  type SectionResult[E <: KoukikoureiError, T] = Validated[List[E], T]

  object KoukikoureiIdValidator
      extends DatabaseIdValidator(KoukikoureiIdError, "koukikourei-id")

  object PatientIdValidator
      extends SectionValidator(PatientIdError, "patient-id"):
    def validate(input: String): Result[Int] =
      inputToInt(input) |> positive

  object HokenshaBangouValidator
      extends SectionValidator(HokenshaBangouError, "保険者番号"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object HihokenshaBangouValidator
      extends SectionValidator(HihokenshaBangouError, "被保険者番号"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object FutanWariValidator extends SectionValidator(FutanWariError, "負担割"):
    def validate(input: String): Result[Int] =
      inputToInt(input) |> (inRange(_, 1, 3))

  object ValidFromValidator extends ValidFromValidator(ValidFromError)

  object ValidUptoValidator
      extends ValidUptoValidator(ValidUptoError, ValidUpto.apply _)

  object ConsistentValidRangeValidator
      extends ConsistentValidRangeValidator[KoukikoureiError, Koukikourei](
        () => InconsistentValidRangeError,
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
  ): Validated[List[KoukikoureiError], Koukikourei] =
    val gKoukikoureiIdResult: SectionResult[KoukikoureiError, Int] =
      koukikoureiIdResult
    val gPatientIdResult: SectionResult[KoukikoureiError, Int] =
      patientIdResult
    val gHokenshaBangouResult: SectionResult[KoukikoureiError, String] =
      hokenshaBangouResult
    val gHihokenshaBangouResult: SectionResult[KoukikoureiError, String] =
      hihokenshaBangouResult
    val gFutanWariResult: SectionResult[KoukikoureiError, Int] =
      futanWariResult
    val gValidFromResult: SectionResult[KoukikoureiError, LocalDate] =
      validFromResult
    val gValidUptoResult: SectionResult[KoukikoureiError, ValidUpto] =
      validUptoResult
    (
      gKoukikoureiIdResult,
      gPatientIdResult,
      gHokenshaBangouResult,
      gHihokenshaBangouResult,
      gFutanWariResult,
      gValidFromResult,
      gValidUptoResult
    ).mapN(Koukikourei.apply _) |> ConsistentValidRangeValidator.validate

// import cats.*
// import cats.syntax.*
// import cats.data.{ValidatedNec, NonEmptyChain}
// import cats.data.Validated.*
// import cats.implicits.*
// import dev.myclinic.scala.model.{Koukikourei, ValidUpto}
// import java.time.LocalDate
// import cats.data.Validated
// import cats.data.Validated.Valid
// import cats.data.Validated.Invalid
// import scala.util.{Try, Success, Failure}
// import dev.fujiwara.validator.ValidatorUtil.*

// object KoukikoureiValidator:
//   sealed trait KoukikoureiError(msg: String) extends ValidationError:
//     def message: String = msg

//   object NonPositiveKoukikoureiId
//       extends KoukikoureiError("Zero koukikourei-id")
//   object NonPositivePatientId extends KoukikoureiError("Zero patient-id")
//   object EmptyHokenshaBangou extends KoukikoureiError("保険者番号が入力されていません。")
//   object NonIntegerHokenshaBangou extends KoukikoureiError("保険者番号が整数でありません。")
//   object NonPositiveHokenshaBangou extends KoukikoureiError("保険者番号が正の整数でありません。")
//   object EmptyHihokenshaBangou extends KoukikoureiError("被保険者番号が入力されていません。")
//   object NonIntegerHihokenshaBangou extends KoukikoureiError("被保険者番号が整数でありません。")
//   object NonPositiveHihokenshaBangou
//       extends KoukikoureiError("被保険者番号が正の整数でありません。")
//   object InvalidFutanWari
//       extends KoukikoureiError("Invalid futan-wari value (should be 1, 2, or 3")
//   object EmptyValidFrom extends KoukikoureiError("期限開始日が入力されていません。")
//   object InconsistentDateRange
//       extends KoukikoureiError("期限開始日が期限終了日のあとになっています。")

//   type SectionResult[E <: KoukikoureiError, T] = Validated[List[E], T]
//   type Result[T] = Validated[List[KoukikoureiError], T]

//   def validateKoukikoureiIdForUpdate(value: Int): Result[Int] =
//     condValid(value > 0, value, NonPositiveKoukikoureiId)

//   def validatePatientId(patientId: Int): Result[Int] =
//     condValid(patientId > 0, patientId, NonPositivePatientId)

//   def validateHokenshaBangou(src: String): Result[String] =
//     isNotEmpty(src, EmptyHokenshaBangou)
//       .andThen(toInt(_, NonIntegerHokenshaBangou))
//       .andThen(isPositive(_, NonPositiveHokenshaBangou))
//       .map(_.toString)

//   def validateHihokenshaBangou(src: String): Result[String] =
//     isNotEmpty(src, EmptyHihokenshaBangou)
//       .andThen(toInt(_, NonIntegerHihokenshaBangou))
//       .andThen(isPositive(_, NonPositiveHihokenshaBangou))
//       .map(_.toString)

//   def validateFutanWari(srcOpt: Option[String]): Result[Int] =
//     isSome(srcOpt, InvalidFutanWari)
//       .andThen(toInt(_, InvalidFutanWari))
//       .andThen(i => condValid(i >= 1 && i <= 3, i, InvalidFutanWari))

//   def validateValidFrom(dateOption: Option[LocalDate]): Result[LocalDate] =
//     isSome(dateOption, EmptyValidFrom)

//   def validateValidUpto(dateOption: Option[LocalDate]): Result[ValidUpto] =
//     Valid(ValidUpto(dateOption))

//   def validateKoukikourei(
//       koukikoureiIdResult: Result[Int],
//       patientIdResult: Result[Int],
//       hokenshaBangouResult: Result[String],
//       hihokenshaBangouResult: Result[String],
//       futanWariResult: Result[Int],
//       validFromResult: Result[LocalDate],
//       validUptoResult: Result[ValidUpto]
//   ): Result[Koukikourei] =
//     (
//       koukikoureiIdResult,
//       patientIdResult,
//       hokenshaBangouResult,
//       hihokenshaBangouResult,
//       futanWariResult,
//       validFromResult,
//       validUptoResult
//     ).mapN(Koukikourei.apply _)
//       .andThen(k =>
//         isConsistentDateRange(
//           k.validFrom,
//           k.validUpto.value,
//           k,
//           InconsistentDateRange
//         )
//       )

//   def validateKoukikoureiForEnter(
//       patientIdResult: Result[Int],
//       hokenshaBangouResult: Result[String],
//       hihokenshaBangouResult: Result[String],
//       futanWariResult: Result[Int],
//       validFromResult: Result[LocalDate],
//       validUptoResult: Result[ValidUpto]
//   ): Result[Koukikourei] =
//     validateKoukikourei(
//       Valid(0),
//       patientIdResult,
//       hokenshaBangouResult,
//       hihokenshaBangouResult,
//       futanWariResult,
//       validFromResult,
//       validUptoResult
//     )
