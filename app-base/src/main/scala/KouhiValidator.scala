package dev.myclinic.scala.web.appbase.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Kouhi, ValidUpto}
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import scala.util.{Try, Success, Failure}
import cats.data.Validated

object KouhiValidator:
  sealed trait KouhiError:
    def message: String

  object NonPositiveKouhiId extends KouhiError:
    def message: String = "Zero kouhi-id"
  object NonPositivePatientId extends KouhiError:
    def message: String = "Zero patient-id"
  object NonIntegerFutanshaBangou extends KouhiError:
    def message: String = "負担者番号が整数でありません。"
  object NonPositiveFutanshaBangou extends KouhiError:
    def message: String = "負担者番号が正の整数でありません。"
  object NonIntegerJukyuushaBangou extends KouhiError:
    def message: String = "受給者番号が整数でありません。"
  object NonPositiveJukyuushaBangou extends KouhiError:
    def message: String = "受給者番号が正の整数でありません。"
  case class InvalidValidFrom[E](err: NonEmptyChain[E], messageOf: E => String)
      extends KouhiError:
    def message: String = err.toList.map("（期限開始）" + messageOf(_)).mkString("\n")
  object NoValidFrom extends KouhiError:
    def message: String = "期限開始が入力されていません。"
  case class InvalidValidUpto[E](err: NonEmptyChain[E], messageOf: E => String)
      extends KouhiError:
    def message: String = err.toList.map("（期限終了）" + messageOf(_)).mkString("\n")

  type Result[T] = Validated[List[KouhiError], T]

  def validateKouhiForEnter(
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[Int],
      jukyuushaBangouResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Kouhi] =
    (
      Valid(0),
      futanshaBangouResult,
      jukyuushaBangouResult,
      validFromResult,
      validUptoResult,
      patientIdResult
    ).mapN(Kouhi.apply _)

  def validateKouhiForUpdate(
      kouhiIdResult: Result[Int],
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[Int],
      jukyuushaBangouResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Kouhi] =
    (
      kouhiIdResult,
      futanshaBangouResult,
      jukyuushaBangouResult,
      validFromResult,
      validUptoResult,
      patientIdResult
    ).mapN(Kouhi.apply _)

// object KouhiValidator:
//   sealed trait KouhiError:
//     def message: String
//   object NonPositiveKouhiId extends KouhiError:
//     def message: String = "Zero kouhi-id"
//   object NonPositivePatientId extends KouhiError:
//     def message: String = "Zero patient-id"
//   object NonIntegerFutanshaBangou extends KouhiError:
//     def message: String = "負担者番号が整数でありません。"
//   object NonPositiveFutanshaBangou extends KouhiError:
//     def message: String = "負担者番号が正の整数でありません。"
//   object NonIntegerJukyuushaBangou extends KouhiError:
//     def message: String = "受給者番号が整数でありません。"
//   object NonPositiveJukyuushaBangou extends KouhiError:
//     def message: String = "受給者番号が正の整数でありません。"
//   case class InvalidValidFrom[E](err: NonEmptyChain[E], messageOf: E => String)
//       extends KouhiError:
//     def message: String = err.toList.map("（期限開始）" + messageOf(_)).mkString("\n")
//   object NoValidFrom extends KouhiError:
//     def message: String = "期限開始が入力されていません。"
//   case class InvalidValidUpto[E](err: NonEmptyChain[E], messageOf: E => String)
//       extends KouhiError:
//     def message: String = err.toList.map("（期限終了）" + messageOf(_)).mkString("\n")

//   type Result[T] = ValidatedNec[KouhiError, T]

//   extension [T] (r: Result[T])
//     def asEither: Either[String, T] =
//       r match {
//         case Valid(t) => Right(t)
//         case Invalid(err) => Left(err.toList.map(_.message).mkString("\n"))
//       }

//   def validateKouhiIdForUpdate(value: Int): Result[Int] =
//     condNec(value > 0, value, NonPositiveKouhiId)

//   def validatePatientId(patientId: Int): Result[Int] =
//     condNec(patientId > 0, patientId, NonPositivePatientId)

//   def validateFutanshaBangou(src: String): Result[Int] =
//     Try(src.toInt) match {
//       case Success(i) =>
//         if i > 0 then validNec(i)
//         else invalidNec(NonPositiveFutanshaBangou)
//       case Failure(_) => invalidNec(NonIntegerFutanshaBangou)
//     }

//   def validateJukyuushaBangou(src: String): Result[Int] =
//     Try(src.toInt) match {
//       case Success(i) =>
//         if i > 0 then validNec(i)
//         else invalidNec(NonPositiveJukyuushaBangou)
//       case Failure(_) => invalidNec(NonIntegerJukyuushaBangou)
//     }

//   def validateValidFrom[E](
//       result: ValidatedNec[E, LocalDate],
//       messageOf: E => String
//   ): Result[LocalDate] =
//     result.fold(
//       err => invalidNec(InvalidValidFrom(err, messageOf)),
//       validNec(_)
//     )

//   def validateValidUpto[E](
//       result: ValidatedNec[E, ValidUpto],
//       messageOf: E => String
//   ): Result[ValidUpto] =
//     result.fold(
//       err => invalidNec(InvalidValidUpto(err, messageOf)),
//       validNec(_)
//     )

//   def validateKouhi(
//       kouhiIdResult: Result[Int],
//       patientIdResult: Result[Int],
//       futanshaBangouResult: Result[Int],
//       jukyuushaBangouResult: Result[Int],
//       validFromResult: Result[LocalDate],
//       validUptoResult: Result[ValidUpto]
//   ): Result[Kouhi] =
//     (
//       kouhiIdResult,
//       futanshaBangouResult,
//       jukyuushaBangouResult,
//       validFromResult,
//       validUptoResult,
//       patientIdResult
//     ).mapN(Kouhi.apply)

//   def validateKouhiForEnter(
//       patientIdResult: Result[Int],
//       futanshaBangouResult: Result[Int],
//       jukyuushaBangouResult: Result[Int],
//       validFromResult: Result[LocalDate],
//       validUptoResult: Result[ValidUpto]
//   ): Result[Kouhi] =
//     validateKouhi(
//       validNec(0),
//       patientIdResult,
//       futanshaBangouResult,
//       jukyuushaBangouResult,
//       validFromResult,
//       validUptoResult
//     )

//   def validateKouhiForUpdate(
//       kouhiId: Int,
//       patientIdResult: Result[Int],
//       futanshaBangouResult: Result[Int],
//       jukyuushaBangouResult: Result[Int],
//       validFromResult: Result[LocalDate],
//       validUptoResult: Result[ValidUpto]
//   ): Result[Kouhi] =
//     validateKouhi(
//       validateKouhiIdForUpdate(kouhiId),
//       patientIdResult,
//       futanshaBangouResult,
//       jukyuushaBangouResult,
//       validFromResult,
//       validUptoResult
//     )
