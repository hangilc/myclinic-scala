package dev.myclinic.scala.web.appbase

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.{Appoint, Patient}
import cats.data.Validated.{validNec, invalidNec, condNec}
import Validators.*
import java.time.{LocalDate, LocalTime}

object AppointValidator:
  sealed trait AppointError:
    def message: String
  case class NonZeroAppointIdError(appointId: Int) extends AppointError:
    def message = s"Non-zero appoint-id: ${appointId}."
  case class InvalidAppointIdError(appointId: Int) extends AppointError:
    def message = s"Invalid appoint-id: ${appointId}."
  object InvalidAppointTimeError extends AppointError:
    def message = "Appoint-time-id is zero."
  case object EmptyNameError extends AppointError:
    def message = "患者名が入力されていません。"
  object IsNotIntPatientIdError extends AppointError:
    def message = "患者番号が整数でありません。"
  object NegativePatientIdError extends AppointError:
    def message = "患者番号が負数です。"
  object InconsistentPatientIdError extends AppointError:
    def message = "患者情報が患者名と一致しません。"

  type Result[T] = ValidatedNec[AppointError, T]

  extension [T](r: Result[T])
    def asEither: Either[String, T] = Validators.toEither(r, _.message)

  def validateAppointIdForEnter(
      appointId: Int
  ): Result[Int] =
    condNec(appointId == 0, appointId, NonZeroAppointIdError(appointId))

  def validateAppointIdForUpdate(
      appointId: Int
  ): Result[Int] =
    condNec(appointId > 0, appointId, InvalidAppointIdError(appointId))

  def validateAppointTimeId(
      appointTimeId: Int
  ): Result[Int] =
    positiveInt(appointTimeId, InvalidAppointTimeError)

  def validateName(name: String): Result[String] =
    nonEmpty(name, EmptyNameError)

  def validatePatientId(patientId: String): Result[Int] =
    if patientId.isEmpty then validNec(0)
    else
      isInt(patientId, IsNotIntPatientIdError)
        .andThen(ival => nonNegativeInt(ival, NegativePatientIdError))

  def validatePatientIdValue(patientId: Int): Result[Int] =
    nonNegativeInt(patientId, NegativePatientIdError)

  def validateMemo(memo: String): Result[String] =
    validNec(memo)

  def validateMemoString(memoString: String): Result[String] =
    validNec(memoString)

  def validatePatientIdConsistency(
      appoint: Appoint,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    if appoint.patientId == 0 then
      condNec(patientOption.isEmpty, appoint, InconsistentPatientIdError)
    else
      patientOption match {
        case None => invalidNec(InconsistentPatientIdError)
        case Some(patient) => {
          val parts = appoint.patientName.split(raw"[ 　]+", 2)
          val b = parts.size == 2 && {
            val last = parts(0)
            last == patient.lastName || last == patient.lastNameYomi
          } && {
            val first = parts(1)
            first == patient.firstName || first == patient.firstNameYomi
          }
          condNec(b, appoint, InconsistentPatientIdError)
        }
      }

  def validate(
      appointIdResult: Result[Int],
      appointTimeIdResult: Result[Int],
      patientNameResult: Result[String],
      patientIdResult: Result[Int],
      memoResult: Result[String],
      patientOption: Option[Patient]
  ): Result[Appoint] =
    (
      appointIdResult,
      appointTimeIdResult,
      patientNameResult,
      patientIdResult,
      memoResult,
    ).mapN(Appoint.apply)
      .andThen(appoint => validatePatientIdConsistency(appoint, patientOption))

  def validate(
      appointIdResult: Result[Int],
      appointTimeIdResult: Result[Int],
      patientNameResult: Result[String],
      patientIdResult: Result[Int],
      memoStringResult: Result[String],
      tags: Set[String],
      patientOption: Option[Patient]
  ): Result[Appoint] =
    (
      appointIdResult,
      appointTimeIdResult,
      patientNameResult,
      patientIdResult,
      memoStringResult,
      validNec(tags)
    ).mapN(Appoint.create)
      .andThen(appoint => validatePatientIdConsistency(appoint, patientOption))

  def validateForEnter(
      appointTimeId: Int,
      nameInput: String,
      patientIdResult: Result[Int],
      memoStringInput: String,
      tags: Set[String],
      patientOption: Option[Patient]
  ): Result[Appoint] =
    validate(
      validNec(0),
      validateAppointTimeId(appointTimeId),
      validateName(nameInput),
      patientIdResult,
      validateMemoString(memoStringInput),
      tags,
      patientOption
    )

  def validateForUpdate(
      appointId: Int,
      appointTimeId: Int,
      nameInput: String,
      patientIdResult: Result[Int],
      memoInput: String,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    validate(
      validateAppointIdForUpdate(appointId),
      validateAppointTimeId(appointTimeId),
      validateName(nameInput),
      patientIdResult,
      validateMemoString(memoInput),
      patientOption
    )

  def validateForUpdate(
      appoint: Appoint,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    validate(
      validateAppointIdForUpdate(appoint.appointId),
      validateAppointTimeId(appoint.appointTimeId),
      validateName(appoint.patientName),
      validatePatientIdValue(appoint.patientId),
      validateMemoString(appoint.memo),
      patientOption
    )
