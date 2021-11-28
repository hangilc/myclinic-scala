package dev.myclinic.scala.validator

import cats.data.ValidatedNec
import cats.implicits.*
import dev.myclinic.scala.model.Patient
import cats.data.Validated.{validNec, invalidNec, condNec}
import dev.myclinic.scala.validator.Validators.*
import java.time.LocalDate

object PatientValidator:
  sealed trait PatientError:
    def message: String

  object EmptyFirstNameError extends PatientError:
    def message: String = "姓が入力されていません。"
  object EmptyLastNameError extends PatientError:
    def message: String = "名が入力されていません。"
  object EmptyFirstNameYomiError extends PatientError:
    def message: String = "姓のよみが入力されていません。"
  object EmptyLastNameYomiError extends PatientError:
    def message: String = "名のよみが入力されていません。"

