package dev.fujiwara.validator.section

import cats.data.Validated
import cats.data.Validated.{Valid, Invalid}

trait Section:
  def name: String

type SectionError[S] = (S, String)

trait SectionValidator(name: String):
  type Error = (SectionValidator, String)
  type Result[T] = Validated[List[Error], T]

  def error[T](msg: String): Result[T] =
    Invalid(List((this, msg)))

  def cond[T](
      test: Boolean,
      validValue: T,
      errMsg: => String
  ): Result[T] =
    if test then Valid(validValue) else error(errMsg)

  def isPositive(i: Int): Result[Int] =
    cond(i > 0, i, s"${name}の値が正の整数でありません。")


enum PatientSection(val name: String) extends SectionValidator(name):
  case PatientIdSection extends PatientSection("patient-id")

def validatePatientId(value: Int): PatientSection#Result[Int] =
  PatientSection.PatientIdSection.isPositive(value)