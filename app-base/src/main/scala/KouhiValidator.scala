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
  case class InvalidValidUpto[E](err: NonEmptyChain[E], messageOf: E => String)
      extends KouhiError:
    def message: String = err.toList.map("（期限終了）" + messageOf(_)).mkString("\n")

  type Result[T] = ValidatedNec[KouhiError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] =
      r match {
        case Valid(t) => Right(t)
        case Invalid(err) =>
          Left(err.toList.map(_.message).mkString("\n"))
      }

  def validateKouhiIdForUpdate(value: Int): Result[Int] =
    condNec(value > 0, value, NonPositiveKouhiId)

  def validatePatientId(patientId: Int): Result[Int] =
    condNec(patientId > 0, patientId, NonPositivePatientId)

  def validateFutanshaBangou(src: String): Result[String] =
    Try(src.toInt) match {
      case Success(i) =>
        if i > 0 then validNec(src)
        else invalidNec(NonPositiveFutanshaBangou)
      case Failure(_) => invalidNec(NonIntegerFutanshaBangou)
    }

  def validateJukyuushaBangou(src: String): Result[String] =
    Try(src.toInt) match {
      case Success(i) =>
        if i > 0 then validNec(src)
        else invalidNec(NonPositiveJukyuushaBangou)
      case Failure(_) => invalidNec(NonIntegerJukyuushaBangou)
    }

  def validateFutanWari(srcOpt: Option[String]): Result[Int] =
    val src = srcOpt.getOrElse("")
    condNec(!(src == null || src.isEmpty), src, InvalidFutanWari)
      .andThen(str => {
        Try(str.toInt) match {
          case Success(i) => validNec(i)
          case Failure(_) => invalidNec(InvalidFutanWari)
        }
      })
      .andThen {
        case i @ (1 | 2 | 3) => validNec(i)
        case _               => invalidNec(InvalidFutanWari)
      }

  def validateValidFrom[E](
      result: ValidatedNec[E, LocalDate],
      messageOf: E => String
  ): Result[LocalDate] =
    result.fold(
      err => invalidNec(InvalidValidFrom(err, messageOf)),
      validNec(_)
    )

  def validateValidUpto[E](
      result: ValidatedNec[E, ValidUpto],
      messageOf: E => String
  ): Result[ValidUpto] =
    result.fold(
      err => invalidNec(InvalidValidUpto(err, messageOf)),
      validNec(_)
    )

  def validateKouhi(
      kouhiIdResult: Result[Int],
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[String],
      jukyuushaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Kouhi] =
    (
      kouhiIdResult,
      patientIdResult,
      futanshaBangouResult,
      jukyuushaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    ).mapN(Kouhi.apply)

  def validateKouhiForEnter(
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[String],
      jukyuushaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Kouhi] =
    validateKouhi(
      validNec(0),
      patientIdResult,
      futanshaBangouResult,
      jukyuushaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    )

  def validateKouhiForUpdate(
      kouhiId: Int,
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[String],
      jukyuushaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Kouhi] =
    validateKouhi(
      validateKouhiIdForUpdate(kouhiId),
      patientIdResult,
      futanshaBangouResult,
      jukyuushaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    )
