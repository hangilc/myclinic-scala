package dev.myclinic.scala.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Shahokokuho, ValidUpto}
import dev.myclinic.scala.validator.Validators.*
import dev.myclinic.scala.validator.{SexValidator, DateValidator}
import java.time.LocalDate
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import dev.fujiwara.domq.DateInput
import dev.fujiwara.domq.DateInput.DateInputError
import scala.util.{Try, Success, Failure}
import scala.runtime.IntRef

object ShahokokuhoValidator:
  sealed trait ShahokokuhoError:
    def message: String
  object NonPositiveShahokokuhoId extends ShahokokuhoError:
    def message: String = "Zero shahokokuho-id"
  object NonPositivePatientId extends ShahokokuhoError:
    def message: String = "Zero patient-id"
  object NonIntegerHokenshaBangou extends ShahokokuhoError:
    def message: String = "保険者番号が整数でありません。"
  object NonPositiveHokenshaBangou extends ShahokokuhoError:
    def message: String = "保険者番号が正の整数でありません。"
  object InvalidHonnin extends ShahokokuhoError:
    def message: String = "Invalid honnin value (should be 0 or 1)"
  case class InvalidValidFrom(err: NonEmptyChain[DateInputError])
      extends ShahokokuhoError:
    def message: String = err.toList.map("（期限開始）" + _.message).mkString("\n")
  case class InvalidValidUpto(err: NonEmptyChain[DateInputError])
      extends ShahokokuhoError:
    def message: String = err.toList.map("（期限終了）" + _.message).mkString("\n")
  object InvalidKourei extends ShahokokuhoError:
    def message: String = "Invalid kourei value (should be 0, 2, or 3"
  object HihokenshaKigouBangouError extends ShahokokuhoError:
    def message: String = "被保険者記号と番号がどちらも空白です。"

  type Result[T] = ValidatedNec[ShahokokuhoError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] =
      r match {
        case Valid(t) => Right(t)
        case Invalid(err) =>
          Left(err.toList.map(_.message).mkString("\n"))
      }

  def validateShahokokuhoIdForUpdate(value: Int): Result[Int] =
    condNec(value > 0, value, NonPositiveShahokokuhoId)
  def validatePatientId(patientId: Int): Result[Int] =
    condNec(patientId > 0, patientId, NonPositivePatientId)
  def validateHokenshaBangou(value: Int): Result[Int] =
    condNec(value > 0, value, NonPositiveHokenshaBangou)
  def validateHokenshaBangouInput(src: String): Result[Int] =
    Try(src.toInt) match {
      case Success(i) => validateHokenshaBangou(i)
      case Failure(_) => invalidNec(NonIntegerHokenshaBangou)
    }
  def validateHihokenshaKigou(src: String): Result[String] =
    validNec(if src == null then "" else src)
  def validateHihokenshaBangou(src: String): Result[String] =
    validNec(if src == null then "" else src)
  def validateHonnin(src: String): Result[Int] =
    condNec(!(src == null || src.isEmpty), src, InvalidHonnin)
      .andThen(str => {
        Try(str.toInt) match {
          case Success(i) => validNec(i)
          case Failure(_) => invalidNec(InvalidHonnin)
        }
      })
      .andThen {
        case i @ (0 | 1) => validNec(i)
        case _           => invalidNec(InvalidHonnin)
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
  def validateKourei(src: String): Result[Int] =
    condNec(!(src == null || src.isEmpty), src, InvalidKourei)
      .andThen(str => {
        Try(str.toInt) match {
          case Success(i) => validNec(i)
          case Failure(_) => invalidNec(InvalidKourei)
        }
      })
      .andThen {
        case i @ (0 | 2 | 3) => validNec(i)
        case _               => invalidNec(InvalidKourei)
      }
  def validateEdaban(src: String): Result[String] =
    validNec(if src == null then "" else src)

  def validateShahokokuhoForEnter(
      patientIdResult: Result[Int],
      hokenshaBangouResult: Result[Int],
      hihokenshaKigouResult: Result[String],
      hihokenshaBangouResult: Result[String],
      honninResult: Result[Int],
      validFromResult: Result[LocalDate],
      validUptoResult: Result[ValidUpto],
      koureiResult: Result[Int],
      edabanResult: Result[String]
  ): Result[Shahokokuho] =
    (
      validNec(0),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaKigouResult,
      hihokenshaBangouResult,
      honninResult,
      validFromResult,
      validUptoResult,
      koureiResult,
      edabanResult
    ).mapN(Shahokokuho.apply)
      .andThen(shaho =>
        if shaho.hihokenshaKigou.isEmpty && shaho.hihokenshaBangou.isEmpty then
          invalidNec(HihokenshaKigouBangouError)
        else validNec(shaho)
      )
