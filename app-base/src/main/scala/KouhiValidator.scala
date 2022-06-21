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
import dev.myclinic.scala.validator.ValidatorUtil.*

object KouhiValidator:
  sealed trait KouhiError extends ValidationError

  object NonPositiveKouhiId extends KouhiError:
    def message: String = "Zero kouhi-id"
  object NonPositivePatientId extends KouhiError:
    def message: String = "Zero patient-id"
  object NonIntegerFutanshaBangou extends KouhiError:
    def message: String = "負担者番号が整数でありません。"
  object EmptyFutanshaBangou extends KouhiError:
    def message: String = "負担者番号が入力されていません。"
  object NonPositiveFutanshaBangou extends KouhiError:
    def message: String = "負担者番号が正の整数でありません。"
  object EmptyJukyuushaBangou extends KouhiError:
    def message: String = "受給者番号が入力されていません。"
  object NonIntegerJukyuushaBangou extends KouhiError:
    def message: String = "受給者番号が整数でありません。"
  object NonPositiveJukyuushaBangou extends KouhiError:
    def message: String = "受給者番号が正の整数でありません。"
  case class InvalidValidFrom[E](err: NonEmptyChain[E], messageOf: E => String)
      extends KouhiError:
    def message: String = err.toList.map("（期限開始）" + messageOf(_)).mkString("\n")
  object EmptyValidFrom extends KouhiError:
    def message: String = "期限開始が入力されていません。"

  type Result[T] = Validated[List[KouhiError], T]

  def validateKouhiIdForUpdate(kouhiId: Int): Result[Int] =
    isPositive(kouhiId, NonPositiveKouhiId)

  def validatePatientId(patientId: Int): Result[Int] =
    isPositive(patientId, NonPositivePatientId)

  def validateFutanshaBangou(src: String): Result[Int] =
    toInt(src, EmptyFutanshaBangou)
      .andThen(isPositive(_, NonIntegerFutanshaBangou))

  def validateJukyuushaBangou(src: String): Result[Int] =
    toInt(src, EmptyJukyuushaBangou)
      .andThen(isPositive(_, NonPositiveJukyuushaBangou))

  def validateValidFrom(dateOption: Option[LocalDate]): Result[LocalDate] =
    isSome(dateOption, EmptyValidFrom)

  def validateKouhiForEnter(
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[Int],
      jukyuushaBangouResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoValue: Option[LocalDate]
  ): Result[Kouhi] =
    (
      Valid(0),
      futanshaBangouResult,
      jukyuushaBangouResult,
      validFromResult,
      Valid(ValidUpto(validUptoValue)),
      patientIdResult
    ).mapN(Kouhi.apply _)

  def validateKouhiForUpdate(
      kouhiIdResult: Result[Int],
      patientIdResult: Result[Int],
      futanshaBangouResult: Result[Int],
      jukyuushaBangouResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoValue: Option[LocalDate]
  ): Result[Kouhi] =
    (
      kouhiIdResult,
      futanshaBangouResult,
      jukyuushaBangouResult,
      validFromResult,
      Valid(ValidUpto(validUptoValue)),
      patientIdResult
    ).mapN(Kouhi.apply _)
