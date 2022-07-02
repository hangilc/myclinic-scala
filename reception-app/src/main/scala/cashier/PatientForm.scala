// package dev.myclinic.scala.web.reception.cashier

// import dev.fujiwara.domq.all.{*, given}
// import scala.language.implicitConversions
// import dev.myclinic.scala.model.*
// import dev.fujiwara.dateinput.DateOptionInput
// import dev.fujiwara.domq.prop.PropsModel
// import dev.myclinic.scala.web.appbase.PatientValidator
// import dev.myclinic.scala.web.appbase.PatientProps

// case class PatientForm(init: Option[Patient]):

//   val props = PatientProps.props
//   val pModel = PropsModel(init)
//   pModel.updateInput(props)
//   val ele = pModel.formPanel(props)


//   def validateForEnter: Either[String, Patient] =
//     ???
//     // import PatientValidator.*
//     // validatePatientForEnter(
//     //   validateLastName(lastNameInput.value),
//     //   validateFirstName(firstNameInput.value),
//     //   validateLastNameYomi(lastNameYomiInput.value),
//     //   validateFirstNameYomi(firstNameYomiInput.value),
//     //   validateSex(sexInput.value),
//     //   validateBirthday(birthdayInput.value),
//     //   validateAddress(addressInput.value),
//     //   validatePhone(phoneInput.value)
//     // ).asEither

//   def validateForUpdate: Either[String, Patient] =
//     ???
//     // import PatientValidator.*
//     // validatePatientForUpdate(
//     //   validatePatientIdOptionForUpdate(init.map(_.patientId)),
//     //   validateLastName(lastNameInput.value),
//     //   validateFirstName(firstNameInput.value),
//     //   validateLastNameYomi(lastNameYomiInput.value),
//     //   validateFirstNameYomi(firstNameYomiInput.value),
//     //   validateSex(sexInput.value),
//     //   validateBirthday(birthdayInput.value),
//     //   validateAddress(addressInput.value),
//     //   validatePhone(phoneInput.value)
//     // ).asEither


