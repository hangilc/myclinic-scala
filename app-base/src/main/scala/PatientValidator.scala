package dev.myclinic.scala.web.appbase

import cats.syntax.all.*
import cats.data.Validated
import cats.data.Validated.*
import dev.fujiwara.validator.section.*
import dev.fujiwara.validator.section.Implicits.*
import dev.myclinic.scala.model.ValidUpto
import java.time.LocalDate
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Sex

object PatientValidator:

  sealed trait PatientError
  object PatientIdError extends PatientError
  object LastNameError extends PatientError
  object FirstNameError extends PatientError
  object LastNameYomiError extends PatientError
  object FirstNameYomiError extends PatientError
  object SexError extends PatientError
  object BirthdayError extends PatientError
  object AddressError extends PatientError
  object PhoneError extends PatientError

  object PatientIdValidator extends DatabaseIdValidator(PatientIdError, "患者番号")
  
  object LastNameValidator extends SectionValidator(LastNameError, "姓"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object FirstNameValidator extends SectionValidator(FirstNameError, "名"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object LastNameYomiValidator extends SectionValidator(LastNameYomiError, "姓（よみ）"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object FirstNameYomiValidator extends SectionValidator(FirstNameYomiError, "名（よみ）"):
    def validate(input: String): Result[String] =
      notEmpty(input)

  object SexValidator extends SectionValidator(SexError, "性別"):
    def validate(sex: Sex): Result[Sex] =
      valid(sex)

  object BirthdayValidator extends SectionValidator(BirthdayError, "生年月日"):
    def validate(date: LocalDate): Result[LocalDate] =
      valid(date)

    def validateOption(dateOpt: Option[LocalDate]): Result[LocalDate] =
      some(dateOpt)

  object AddressValidator extends SectionValidator(AddressError, "住所"):
    def validate(address: String): Result[String] =
      notNull(address)

  object PhoneValidator extends SectionValidator(PhoneError, "住所"):
    def validate(phone: String): Result[String] =
      notNull(phone)

  def validate(rs: (
    ValidatedResult[PatientIdError.type, Int],
    ValidatedResult[LastNameError.type, String],
    ValidatedResult[FirstNameError.type, String],
    ValidatedResult[LastNameYomiError.type, String],
    ValidatedResult[FirstNameYomiError.type, String],
    ValidatedResult[SexError.type, Sex],
    ValidatedResult[BirthdayError.type, LocalDate],
    ValidatedResult[AddressError.type, String],
    ValidatedResult[PhoneError.type, String],
  )): ValidatedResult[PatientError, Patient] =
    val results = ((Valid(1): ValidatedResult[PatientError, Int]) *: rs).tupled.map(_.tail)
    results.map(Patient.apply.tupled)



// import cats.*
// import cats.syntax.*
// import cats.data.Validated
// import cats.data.Validated.*
// import cats.implicits.*
// import dev.myclinic.scala.model.{Patient, Sex}
// import Validators.*
// import java.time.LocalDate
// import cats.data.Validated.Valid
// import cats.data.Validated.Invalid
// import scala.quoted.Type
// import dev.fujiwara.validator.ValidatorUtil.*

// object PatientValidator:
//   sealed trait PatientError extends ValidationError

//   object EmptyPatientIdError extends PatientError:
//     def message: String = "patient-id is not available"

//   object NonZeroPatientIdError extends PatientError:
//     def message: String = "Non-zero patient-id"
//   object ZeroPatientIdError extends PatientError:
//     def message: String = "Zero patient-id"
//   object EmptyFirstNameError extends PatientError:
//     def message: String = "姓が入力されていません。"
//   object EmptyLastNameError extends PatientError:
//     def message: String = "名が入力されていません。"
//   object EmptyFirstNameYomiError extends PatientError:
//     def message: String = "姓のよみが入力されていません。"
//   object EmptyLastNameYomiError extends PatientError:
//     def message: String = "名のよみが入力されていません。"
//   case class SexError(sexError: List[SexValidator.SexError]) extends PatientError:
//     def message: String = sexError(0).message
//   object EmptyBirthday extends PatientError:
//     def message: String = "生年月日が入力されていません。"

//   type Result[T] = Validated[List[PatientError], T]

//   def validatePatientIdForUpdate(patientId: Int): Result[Int] =
//     condValid(patientId != 0, patientId, ZeroPatientIdError)
//   def validatePatientIdOptionForUpdate(patientIdOption: Option[Int]): Result[Int] =
//     isSome(patientIdOption, EmptyPatientIdError)
//       .andThen(validatePatientIdForUpdate(_))
//   def validateLastName(input: String): Result[String] =
//     isNotEmpty(input, EmptyLastNameError)
//   def validateFirstName(input: String): Result[String] =
//     isNotEmpty(input, EmptyFirstNameError)
//   def validateLastNameYomi(input: String): Result[String] =
//     isNotEmpty(input, EmptyLastNameYomiError)
//   def validateFirstNameYomi(input: String): Result[String] =
//     isNotEmpty(input, EmptyFirstNameYomiError)
//   def validateSexInput(input: Option[String]): Result[Sex] =
//     SexValidator.validateSexInput(input).leftMap(err => List(SexError(err)))
//   def validateSex(sex: Sex): Result[Sex] = Valid(sex)
//   def validateBirthday(dateOption: Option[LocalDate]): Result[LocalDate] =
//     isSome(dateOption, EmptyBirthday)
//   def validateAddress(input: String): Result[String] = Valid(input)
//   def validatePhone(input: String): Result[String] = Valid(input)

//   def validatePatientForEnter(
//       lastNameResult: Result[String],
//       firstNameResult: Result[String],
//       lastNameYomiResult: Result[String],
//       firstNameYomiResult: Result[String],
//       sexResult: Result[Sex],
//       birthdayResult: Result[LocalDate],
//       addressResult: Result[String],
//       phoneResult: Result[String]
//   ): Result[Patient] =
//     (
//       Valid(0),
//       lastNameResult,
//       firstNameResult,
//       lastNameYomiResult,
//       firstNameYomiResult,
//       sexResult,
//       birthdayResult,
//       addressResult,
//       phoneResult
//     )
//       .mapN(Patient.apply _)

//   def validatePatientForUpdate(
//       patientIdResult: Result[Int],
//       lastNameResult: Result[String],
//       firstNameResult: Result[String],
//       lastNameYomiResult: Result[String],
//       firstNameYomiResult: Result[String],
//       sexResult: Result[Sex],
//       birthdayResult: Result[LocalDate],
//       addressResult: Result[String],
//       phoneResult: Result[String]
//   ): Result[Patient] =
//     (
//       patientIdResult,
//       lastNameResult,
//       firstNameResult,
//       lastNameYomiResult,
//       firstNameYomiResult,
//       sexResult,
//       birthdayResult,
//       addressResult,
//       phoneResult
//     )
//       .mapN(Patient.apply _)
