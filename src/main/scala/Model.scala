package dev.myclinic.scala.model

import java.time.LocalDate

sealed trait Sex
case object Male extends Sex
case object Female extends Sex

case class Patient(patientId: Int, lastName: String, firstName: String,
  lastNameYomi: String, firstNameYomi: String, sex: Sex,
  birthday: LocalDate, address: String, phone: String)

