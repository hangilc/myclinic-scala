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

  type Result = ValidatedNec[AppointTimeError, AppointTime]

  def validateKind(input: String): ValidatedNec[AppointTimeError, String] =
    nonEmpty(input, KindEmptyError)

  def validateCapacity(input: String): ValidatedNec[AppointTimeError, Int] =
    nonEmpty(input, CapacityEmptyError)
      .andThen(s => isInt(s, CapacityNumberFormatError))
      .andThen(i => nonNegativeInt(i, CapacityNegativeError))

  def validate(
      date: LocalDate,
      fromTime: LocalTime,
      untilTime: LocalTime,
      kindInput: String,
      capacityInput: String
  ): Result =
    val checkTimes = timeIsBeforeOrEqual(fromTime, untilTime, TimesOrderError)
    (checkTimes, validateKind(kindInput), validateCapacity(capacityInput))
      .mapN(_, AppointTime(0, date, fromTime, untilTime, _, _))
