package dev.myclinic.scala.model

import java.time.LocalDate

case class Patient(
    patientId: Int,
    lastName: String,
    firstName: String,
    lastNameYomi: String,
    firstNameYomi: String,
    sex: Sex,
    birthday: LocalDate,
    address: String,
    phone: String
)
