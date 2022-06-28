package dev.fujiwara.validator.section

import cats.*
import cats.syntax.all.*
import cats.data.Validated
import cats.data.Validated.{Valid, Invalid}

type SectionError[+S] = (S, String)
type ValidatedSection[+S, +T] = Validated[List[SectionError[S]], T]

trait SectionValidator[S](name: String):
  this : S =>
  type Result[T] = ValidatedSection[S, T]
  def error[T](msg: String): Result[T] =
    Invalid(List((this, msg)))

  def notEmpty(src: String): Result[String] =
    if src == null || src.isEmpty then error(s"${name}が入力されていません。")
    else Valid(src)

sealed trait PatientValidator

type Result[T] = ValidatedSection[PatientValidator, T]

object PatientIdValidator extends SectionValidator[PatientIdValidator.type]("patient-id") with PatientValidator:

  def validateForEnter: Result[Int] =
    error("invalid")

object LastNameValidator extends SectionValidator[LastNameValidator.type]("姓") with PatientValidator:
  def validate(src: String): Result[String] =
    notEmpty(src)

object Validate:
  def validate(
    patientIdResult: ValidatedSection[PatientIdValidator.type, Int],
    lastNameResult: ValidatedSection[LastNameValidator.type, String]
  ): ValidatedSection[PatientValidator, (Int, String)] =
    val gPatientIdREsult: Result[Int] = patientIdResult
    val gLastNameResult: Result[String] = lastNameResult
    (gPatientIdREsult, gLastNameResult).mapN((a, b) => (a, b))


