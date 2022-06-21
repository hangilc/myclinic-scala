package dev.myclinic.scala.web.appbase.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Koukikourei, ValidUpto}
import java.time.LocalDate
import cats.data.Validated
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import scala.util.{Try, Success, Failure}
import dev.myclinic.scala.validator.ValidatorUtil.*

object KoukikoureiValidator:
  sealed trait KoukikoureiError extends ValidationError

  object NonPositiveKoukikoureiId extends KoukikoureiError:
    def message: String = "Zero koukikourei-id"
  object NonPositivePatientId extends KoukikoureiError:
    def message: String = "Zero patient-id"
  object NonIntegerHokenshaBangou extends KoukikoureiError:
    def message: String = "保険者番号が整数でありません。"
  object NonPositiveHokenshaBangou extends KoukikoureiError:
    def message: String = "保険者番号が正の整数でありません。"
  object NonIntegerHihokenshaBangou extends KoukikoureiError:
    def message: String = "被保険者番号が整数でありません。"
  object NonPositiveHihokenshaBangou extends KoukikoureiError:
    def message: String = "被保険者番号が正の整数でありません。"
  object InvalidFutanWari extends KoukikoureiError:
    def message: String = "Invalid futan-wari value (should be 1, 2, or 3"
  object EmptyValidFrom extends KoukikoureiError:
    def message: String = "期限開始日が入力されていません。"

  type Result[T] = Validated[List[KoukikoureiError], T]

  def validateKoukikoureiIdForUpdate(value: Int): Result[Int] =
    condValid(value > 0, value, NonPositiveKoukikoureiId)

  def validatePatientId(patientId: Int): Result[Int] =
    condValid(patientId > 0, patientId, NonPositivePatientId)

  def validateHokenshaBangou(src: String): Result[String] =
    Try(src.toInt) match {
      case Success(i) =>
        if i > 0 then validNec(src)
        else invalidNec(NonPositiveHokenshaBangou)
      case Failure(_) => invalidNec(NonIntegerHokenshaBangou)
    }

  def validateHihokenshaBangou(src: String): Result[String] =
    Try(src.toInt) match {
      case Success(i) =>
        if i > 0 then validNec(src)
        else invalidNec(NonPositiveHihokenshaBangou)
      case Failure(_) => invalidNec(NonIntegerHihokenshaBangou)
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

  def validateKoukikourei(
      koukikoureiIdResult: Result[Int],
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Koukikourei] =
    (
      koukikoureiIdResult,
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    ).mapN(Koukikourei.apply)

  def validateKoukikoureiForEnter(
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Koukikourei] =
    validateKoukikourei(
      validNec(0),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    )

  def validateKoukikoureiForUpdate(
      koukikoureiId: Int,
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Koukikourei] =
    validateKoukikourei(
      validateKoukikoureiIdForUpdate(koukikoureiId),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    )
