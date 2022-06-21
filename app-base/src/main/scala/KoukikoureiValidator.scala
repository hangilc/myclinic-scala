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
  sealed trait KoukikoureiError(msg: String) extends ValidationError:
    def message: String = msg

  object NonPositiveKoukikoureiId
      extends KoukikoureiError("Zero koukikourei-id")
  object NonPositivePatientId extends KoukikoureiError("Zero patient-id")
  object EmptyHokenshaBangou extends KoukikoureiError("保険者番号が入力されていません。")
  object NonIntegerHokenshaBangou extends KoukikoureiError("保険者番号が整数でありません。")
  object NonPositiveHokenshaBangou extends KoukikoureiError("保険者番号が正の整数でありません。")
  object EmptyHihokenshaBangou extends KoukikoureiError("被保険者番号が入力されていません。")
  object NonIntegerHihokenshaBangou extends KoukikoureiError("被保険者番号が整数でありません。")
  object NonPositiveHihokenshaBangou
      extends KoukikoureiError("被保険者番号が正の整数でありません。")
  object InvalidFutanWari
      extends KoukikoureiError("Invalid futan-wari value (should be 1, 2, or 3")
  object EmptyValidFrom extends KoukikoureiError("期限開始日が入力されていません。")
  object InconsistentDateRange
      extends KoukikoureiError("期限開始日が期限終了日のあとになっています。")

  type Result[T] = Validated[List[KoukikoureiError], T]

  def validateKoukikoureiIdForUpdate(value: Int): Result[Int] =
    condValid(value > 0, value, NonPositiveKoukikoureiId)

  def validatePatientId(patientId: Int): Result[Int] =
    condValid(patientId > 0, patientId, NonPositivePatientId)

  def validateHokenshaBangou(src: String): Result[String] =
    isNotEmpty(src, EmptyHokenshaBangou)
      .andThen(toInt(_, NonIntegerHokenshaBangou))
      .andThen(isPositive(_, NonPositiveHokenshaBangou))
      .map(_.toString)

  def validateHihokenshaBangou(src: String): Result[String] =
    isNotEmpty(src, EmptyHihokenshaBangou)
      .andThen(toInt(_, NonIntegerHihokenshaBangou))
      .andThen(isPositive(_, NonPositiveHihokenshaBangou))
      .map(_.toString)

  def validateFutanWari(srcOpt: Option[String]): Result[Int] =
    isSome(srcOpt, InvalidFutanWari)
      .andThen(toInt(_, InvalidFutanWari))
      .andThen(i => condValid(i >= 1 && i <= 3, i, InvalidFutanWari))

  def validateValidFrom(dateOption: Option[LocalDate]): Result[LocalDate] =
    isSome(dateOption, EmptyValidFrom)

  def validateKoukikourei(
      koukikoureiId: Int,
      patientId: Int,
      hokenshaBangou: String,
      hihokenshaBangou: String,
      futanWari: Int,
      validFrom: LocalDate,
      validUpto: ValidUpto
  ): Result[Koukikourei] =
    isConsistentDateRange(validFrom, validUpto.value, InconsistentDateRange)
      .andThen(_ =>
        Valid(
          Koukikourei(
            koukikoureiId,
            patientId,
            hokenshaBangou,
            hihokenshaBangou,
            futanWari,
            validFrom,
            validUpto
          )
        )
      )

  def validateKoukikoureiForEnter(
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Koukikourei] =
    (
      validNec(0),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    ).mapN(validateKoukikourei _)

  def validateKoukikoureiForUpdate(
      koukikoureiId: Int,
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      futanWariResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto]
  ): Result[Koukikourei] =
    (
      validateKoukikoureiIdForUpdate(koukikoureiId),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaBangouResult,
      futanWariResult,
      validFromResult,
      validUptoResult
    ).mapN(validateKoukikourei _)
