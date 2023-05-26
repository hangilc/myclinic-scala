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
  phone: String,
  memo: Option[String] = None,
):
  def fullName(sep: String = " "): String = 
      s"${lastName}${sep}${firstName}"
  def fullNameYomi(sep: String = " "): String =
      s"${lastNameYomi}${sep}${firstNameYomi}"

object Patient:
  val modelSymbol = "patient"
  given ModelSymbol[Patient] with
    def getSymbol = modelSymbol
  given DataId[Patient] = _.patientId