package dev.fujiwara.validator.section

import cats.data.Validated
import cats.data.Validated.{Valid, Invalid}

type ValidatedSection[S, T] = Validated[List[(S, String)], T]

trait SectionValidator[S]:
  this : S =>
  def error[T](msg: String): ValidatedSection[S, T] =
    Invalid(List((this, msg)))

sealed trait PatientValidator

object PatientIdValidator extends SectionValidator[PatientIdValidator.type] with PatientValidator:

  type ValidatedPatientId = ValidatedSection[PatientIdValidator.type, Int]

  def validateForEnter: ValidatedPatientId =
    error("invalid")

