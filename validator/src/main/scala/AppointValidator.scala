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

  case class Result[T](result: ValidatedNec[AppointError, T]):
    def toEither(): Either[String, T] =
      Validators.toEither(result, _.message)

  def validateAppointIdForEnter(
      appointId: Int
  ): Result[Int] =
    Result(condNec(appointId == 0, appointId, NonZeroAppointIdError(appointId)))

  def validateAppointIdForUpdate(
    appointId: Int
  ): Result[Int] =
    Result(condNec(appointId > 0, appointId, InvalidAppointIdError(appointId)))

  def validateAppointTimeId(
      appointTimeId: Int
  ): Result[Int] =
    Result(positiveInt(appointTimeId, InvalidAppointTimeError))

  def validateName(name: String): Result[String] =
    Result(nonEmpty(name, EmptyNameError))

  def validatePatientId(patientId: String): Result[Int] =
    Result(if patientId.isEmpty then validNec(0)
    else
      isInt(patientId, IsNotIntPatientIdError)
        .andThen(ival => nonNegativeInt(ival, NegativePatientIdError)))

  def validatePatientIdValue(patientId: Int): Result[Int] =
    Result(nonNegativeInt(patientId, NegativePatientIdError))

  def validateMemo(memo: String): Result[String] =
    Result(validNec(memo))

  def validatePatientIdConsistency(
      appoint: Appoint,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    if appoint.patientId == 0 then
      Result(condNec(patientOption.isEmpty, appoint, InconsistentPatientIdError))
    else
      patientOption match {
        case None => Result(invalidNec(InconsistentPatientIdError))
        case Some(patient) => {
          val parts = appoint.patientName.split(raw"[ 　]+", 2)
          val b = parts.size == 2 && {
            val last = parts(0)
            last == patient.lastName || last == patient.lastNameYomi
          } && {
            val first = parts(1)
            first == patient.firstName || first == patient.firstNameYomi
          }
          Result(condNec(b, appoint, InconsistentPatientIdError))
        }
      }


  def validateForEnter(
      appointId: Int,
      appointTimeId: Int,
      nameInput: String,
      patientId: String,
      memoInput: String,
      patientOption: Option[Patient]
  ): Result[Appoint] =
    val r = (
      validateAppointIdForEnter(appointId).result,
      validateAppointTimeId(appointTimeId).result,
      validateName(nameInput).result,
      validatePatientId(patientId).result,
      validateMemo(memoInput).result
    ).mapN(Appoint.apply)
    .andThen(appoint => validatePatientIdConsistency(appoint, patientOption).result)
    Result(r)

  def validateForUpate(appoint: Appoint, patientOption: Option[Patient]): Result[Appoint] =
    val r = (
      validateAppointIdForUpdate(appoint.appointId).result,
      validateAppointTimeId(appoint.appointTimeId).result,
      validateName(appoint.patientName).result,
      validatePatientIdValue(appoint.patientId).result,
      validateMemo(appoint.memo).result
    ).tupled.andThen(_ => validatePatientIdConsistency(appoint, patientOption).result)
    Result(r)
