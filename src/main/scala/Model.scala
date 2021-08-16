package dev.myclinic.scala.model

import java.time.{LocalDate, LocalDateTime}

sealed trait Sex
case object Male extends Sex
case object Female extends Sex

case class Patient(patientId: Int, lastName: String, firstName: String,
  lastNameYomi: String, firstNameYomi: String, sex: Sex,
  birthday: LocalDate, address: String, phone: String)

case class Visit(visitId: Int, patientId: Int, visitedAt: LocalDateTime,
  shahokokuhoId: Int, roujinId: Int, kouhi1Id: Int, kouhi2Id: Int, 
  kouhi3Id: Int, koukikoureiId: Int, attrib: String)

