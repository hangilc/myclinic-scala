package dev.myclinic.scala.web.appbase.validator

import cats.*
import cats.syntax.*
import cats.data.{ValidatedNec, NonEmptyChain}
import cats.data.Validated.*
import cats.implicits.*
import dev.myclinic.scala.model.{Shahokokuho, ValidUpto}
import java.time.LocalDate
import cats.data.Validated
import cats.data.Validated.*
import scala.util.{Try, Success, Failure}
import dev.fujiwara.validator.ValidatorUtil.*

object ShahokokuhoValidator:

  sealed class ShahokokuhoError(val group: ErrorGroup, msg: String)
      extends ValidationError:
    def message: String = msg

  enum ErrorGroup(name: String):
    import dev.fujiwara.validator.ValidatorUtil.ErrorMessages.*
    def apply(msg: String): ShahokokuhoError =
      new ShahokokuhoError(this, msg)
    def empty: ShahokokuhoError = apply(isEmptyErrorMessage(name))
    def notInteger: ShahokokuhoError = apply(notIntegerErrorMessage(name))
    def notPositive: ShahokokuhoError = apply(notPositiveErrorMessage(name))
    def invalid: ShahokokuhoError = apply(invalidValueErrorMessage(name))

    case ShahokokuhoIdError extends ErrorGroup("shahokokuhoId")
    case PatientIdError extends ErrorGroup("patientId")
    case HokenshaBangouError extends ErrorGroup("保険者番号")
    case HonninError extends ErrorGroup("本人")
    case ValidFromError extends ErrorGroup("期限開始日")
    case ValidUptoError extends ErrorGroup("期限終了日")
    case KoureiError extends ErrorGroup("高齢")
    case InconsistentHihokenshaError extends ErrorGroup("被保険者記号・番号")

  type Result[T] = Validated[List[ShahokokuhoError], T]

  extension [T] (r: Result[T])
    def asEither: Either[String, T] =
      r.toEither.left.map(_.map(_.message).mkString("\n"))

  import ErrorGroup.*

  def validateShahokokuhoIdForUpdate(value: Int): Result[Int] =
    condValid(value > 0, value, ShahokokuhoIdError.notPositive)
  
  def validateShahokokuhoIdOptionForUpdate(value: Option[Int]): Result[Int] =
    isSome(value, ShahokokuhoIdError.empty)
      .andThen(validateShahokokuhoIdForUpdate(_))
  
  def validatePatientId(patientId: Int): Result[Int] =
    condValid(patientId > 0, patientId, PatientIdError.notPositive)

  def validatePatientIdOption(patientIdOption: Option[Int]): Result[Int] =
    isSome(patientIdOption, PatientIdError.empty)
      .andThen(validatePatientId(_))

  def validateHokenshaBangou(value: Int): Result[Int] =
    condValid(value > 0, value, HokenshaBangouError.notPositive)
  
  def validateHokenshaBangouInput(src: String): Result[Int] =
    isNotEmpty(src, HokenshaBangouError.empty)
      .andThen(toInt(_, HokenshaBangouError.notInteger))
      .andThen(validateHokenshaBangou(_))

  def validateHihokenshaKigou(src: String): Result[String] = nonNullString(src)

  def validateHihokenshaBangou(src: String): Result[String] = nonNullString(src)
  
  def validateHonnin(value: Int): Result[Int] =
    isOneOf(value, List(0, 1), HonninError.invalid)

  def validateHonninInput(srcOpt: Option[String]): Result[Int] =
    isSome(srcOpt, HonninError.empty)
      .andThen(toInt(_, HonninError.notInteger))
      .andThen(validateHonnin(_))

  def validateValidFrom(dateOption: Option[LocalDate]): Result[LocalDate] =
    isSome(dateOption, ValidFromError.empty)

  def validateValidUpto(dateOption: Option[LocalDate]): Result[ValidUpto] = 
    Valid(ValidUpto(dateOption))

  def validateKourei(value: Int): Result[Int] =
    isOneOf(value, List(0, 2, 3), KoureiError.invalid)

  def validateKoureiInput(srcOpt: Option[String]): Result[Int] =
    isSome(srcOpt, KoureiError.empty)
      .andThen(toInt(_, KoureiError.notInteger))
      .andThen(validateKourei(_))

  def validateEdaban(src: String): Result[String] = nonNullString(src)

  def validateShahokokuho(
      shahokokuhoIdResult: Result[Int],
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
      shahokokuhoIdResult,
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
          Invalid(List(InconsistentHihokenshaError("被保険者記号・番号が両方空白です。")))
        else Valid(shaho)
      )

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
    validateShahokokuho(
      Valid(0),
      patientIdResult,
      hokenshaBangouResult,
      hihokenshaKigouResult,
      hihokenshaBangouResult,
      honninResult,
      validFromResult,
      validUptoResult,
      koureiResult,
      edabanResult
    )
