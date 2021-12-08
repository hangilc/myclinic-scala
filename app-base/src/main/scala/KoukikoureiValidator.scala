package dev.myclinic.scala.web.appbase.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Koukikourei, ValidUpto}
import dev.myclinic.scala.validator.Validators.*
import dev.myclinic.scala.validator.{SexValidator, DateValidator}
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.fujiwara.domq.DateInput
import dev.fujiwara.domq.DateInput.DateInputError
import scala.util.{Try, Success, Failure}
import scala.runtime.IntRef

object KoukikoureiValidator:
  sealed trait KoukikoureiError:
    def message: String
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
  object HihokenshaKigouBangouError extends KoukikoureiError:
    def message: String = "被保険者記号と番号がどちらも空白です。"
  case class InvalidValidFrom(err: NonEmptyChain[DateInputError])
      extends KoukikoureiError:
    def message: String = err.toList.map("（期限開始）" + _.message).mkString("\n")
  case class InvalidValidUpto(err: NonEmptyChain[DateInputError])
      extends KoukikoureiError:
    def message: String = err.toList.map("（期限終了）" + _.message).mkString("\n")

  type Result[T] = ValidatedNec[KoukikoureiError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] =
      r match {
        case Valid(t) => Right(t)
        case Invalid(err) =>
          Left(err.toList.map(_.message).mkString("\n"))
      }

  def validateKoukikoureiIdForUpdate(value: Int): Result[Int] =
    condNec(value > 0, value, NonPositiveKoukikoureiId)
  def validatePatientId(patientId: Int): Result[Int] =
    condNec(patientId > 0, patientId, NonPositivePatientId)
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
  def validateValidFrom(src: String): Result[LocalDate] =
    DateInput.validateDateInput(src) match {
      case Valid(d)     => validNec(d)
      case Invalid(err) => invalidNec(InvalidValidFrom(err))
    }
  def validateValidUpto(src: String): Result[ValidUpto] =
    if src == null || src.isEmpty || src == "0000-00-00" then
      validNec(ValidUpto(None))
    else
      DateInput.validateDateInput(src) match {
        case Valid(d)     => validNec(ValidUpto(Some(d)))
        case Invalid(err) => invalidNec(InvalidValidUpto(err))
      }

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