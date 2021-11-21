package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.AppointTime
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
import java.time.{LocalDate, LocalTime}

object AppointTimeValidator:
  sealed trait AppointTimeError:
    def message: String
  object NonZeroAppointTimeIdError extends AppointTimeError:
    def message = s"Appoint-time-id is not zero."
  object NonPositiveAppointTimeIdError extends AppointTimeError:
    def message = s"Appoint-time-id is not positive."
  object InvalidFromTimeInputError extends AppointTimeError:
    def message = s"開始時刻の入力が時刻でありません。"
  object InvalidUntilTimeInputError extends AppointTimeError:
    def message = s"終了時刻の入力が時刻でありません。"
  case object KindEmptyError extends AppointTimeError:
    def message: String = "種類が入力されていません。"
  case object CapacityEmptyError extends AppointTimeError:
    def message: String = "定員が入力されていません。"
  case object CapacityNumberFormatError extends AppointTimeError:
    def message: String = "定員の入力が整数値でありません。"
  case object CapacityNegativeError extends AppointTimeError:
    def message: String = "定員の入力が負数です。"
  case object TimesOrderError extends AppointTimeError:
    def message: String = "開始時刻と終了時刻の関係が不適切です。"

  type Result[T] = ValidatedNec[AppointTimeError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] = Validators.toEither(r, _.message)

  def validateAppointTimeIdForCreate(appointTimeId: Int): Result[Int] =
    condNec(appointTimeId == 0, appointTimeId, NonZeroAppointTimeIdError)

  def validateAppointTimeIdForUpdate(appointTimeId: Int): Result[Int] =
    positiveInt(appointTimeId, NonPositiveAppointTimeIdError)

  def validateDateValue(date: LocalDate): Result[LocalDate] =
    validNec(date)

  def validateFromTimeInput(input: String): Result[LocalTime] =
    isLocalTime(input, InvalidFromTimeInputError)

  def validateFromTimeValue(time: LocalTime): Result[LocalTime] =
    validNec(time)

  def validateUntilTimeInput(input: String): Result[LocalTime] =
    isLocalTime(input, InvalidUntilTimeInputError)

  def validateUntilTimeValue(time: LocalTime): Result[LocalTime] =
    validNec(time)

  def validateKindInput(input: String): Result[String] =
    nonEmpty(input, KindEmptyError)

  def validateCapacityInput(
      input: String
  ): Result[Int] =
    nonEmpty(input, CapacityEmptyError)
      .andThen(s => isInt(s, CapacityNumberFormatError))
      .andThen(i => validateCapacityValue(i))

  def validateCapacityValue(capacity: Int): Result[Int] =
    nonNegativeInt(capacity, CapacityNegativeError)

  def validateTimes(appointTime: AppointTime): Result[AppointTime] =
    timeIsBeforeOrEqual(
      appointTime.fromTime,
      appointTime.untilTime,
      appointTime,
      TimesOrderError
    )

  def validateForUpdate(
      appointTimeId: Int,
      dateValidate: Result[LocalDate],
      fromTimeValidate: Result[LocalTime],
      untilTimeValidate: Result[LocalTime],
      kindValidate: Result[String],
      capacityValidate: Result[Int]
  ): Result[AppointTime] =
    var r = (
      validateAppointTimeIdForUpdate(appointTimeId),
      dateValidate,
      fromTimeValidate,
      untilTimeValidate,
      kindValidate,
      capacityValidate
    ).mapN(AppointTime.apply)
    r.andThen(a => validateTimes(a))

  def validateForEnter(
      date: LocalDate,
      fromTimeValidate: Result[LocalTime],
      untilTimeValidate: Result[LocalTime],
      kindValidate: Result[String],
      capacityValidate: Result[Int]
  ): Result[AppointTime] =
    (
      validNec(0),
      validNec(date),
      fromTimeValidate,
      untilTimeValidate,
      kindValidate,
      capacityValidate
    ).mapN(AppointTime.apply)
      .andThen(a => validateTimes(a))
