package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.Appoint
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
import java.time.{LocalDate, LocalTime}

object AppointValidator:
  sealed trait AppointError:
    def message: String
  case class NonZeroAppointIdError(appointId: Int) extends AppointError:
    def message = s"Non-zero appoint-id for enter: ${appointId}."
  object InvalidAppointTimeError extends AppointError:
    def message = "Appoint-time-id is zero."
  case object EmptyNameError extends AppointError:
    def message = "患者名が入力されていません。"
  object IsNotIntPatientIdError extends AppointError:
    def message = "患者番号が整数でありません。"
  object NegativePatientIdError extends AppointError:
    def message = "患者番号が負数です。"

  case class Result(value: ValidatedNec[AppointError, Appoint]):
    def toEither(): Either[String, Appoint] =
      Validators.toEither(value, _.message)

  def validateAppointIdForEnter(
      appointId: Int
  ): ValidatedNec[AppointError, Int] =
    condNec(appointId == 0, appointId, NonZeroAppointIdError(appointId))

  def validateAppointTimeId(
      appointTimeId: Int
  ): ValidatedNec[AppointError, Int] =
    positiveInt(appointTimeId, InvalidAppointTimeError)

  def validateName(name: String): ValidatedNec[AppointError, String] =
    nonEmpty(name, EmptyNameError)

  def validatePatientId(patientId: String): ValidatedNec[AppointError, Int] =
    if patientId.isEmpty then validNec(0)
    else
      isInt(patientId, IsNotIntPatientIdError)
        .andThen(ival => nonNegativeInt(ival, NegativePatientIdError))

  def validateMemo(memo: String): ValidatedNec[AppointError, String] =
    validNec(memo)

  def validateForEnter(
      appointId: Int,
      appointTimeId: Int,
      nameInput: String,
      patientId: String,
      memoInput: String
  ): Result =
    val v = (
      validateAppointIdForEnter(appointId),
      validateAppointTimeId(appointTimeId),
      validateName(nameInput),
      validatePatientId(patientId),
      validateMemo(memoInput)
    ).mapN(Appoint.apply)
    Result(v)
