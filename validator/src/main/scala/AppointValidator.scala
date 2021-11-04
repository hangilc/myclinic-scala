package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.{Appoint, Patient}
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
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
    def toEither(): Either[String, T] = Validators.toEither(r, _.message)

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

  def validateForEnter(
      appointTimeId: Int,
      nameInput: String,
      patientIdResult: Result[Int],
      memoInput: String,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    (
      validNec(0),
      validateAppointTimeId(appointTimeId),
      validateName(nameInput),
      patientIdResult,
      validateMemo(memoInput)
    ).mapN(Appoint.apply)
      .andThen(appoint =>
        validatePatientIdConsistency(appoint, patientOption)
      )

  def validateForUpate(
      appoint: Appoint,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    (
      validateAppointIdForUpdate(appoint.appointId),
      validateAppointTimeId(appoint.appointTimeId),
      validateName(appoint.patientName),
      validatePatientIdValue(appoint.patientId),
      validateMemo(appoint.memo)
    ).tupled.andThen(_ =>
      validatePatientIdConsistency(appoint, patientOption)
    )
