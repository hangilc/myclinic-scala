package dev.myclinic.scala.javalib.convs

import dev.myclinic.vertx.dto.*
import dev.myclinic.scala.model.*
import java.time.LocalDate

object Convs:
  extension (p: PatientDTO)
    def toScala: Patient =
      Patient(
        p.patientId,
        p.lastName,
        p.firstName,
        p.lastNameYomi,
        p.firstNameYomi,
        Sex.fromCode(p.sex),
        LocalDate.parse(p.birthday),
        p.address,
        p.phone
      )

  extension(p: Patient)
    def toDTO: PatientDTO =
      val dto = new PatientDTO()
      dto.patientId = p.patientId
      dto.lastName = p.lastName
      dto.firstName = p.firstName
      dto.lastNameYomi = p.lastNameYomi
      dto.firstNameYomi = p.firstNameYomi
      dto.sex = p.sex.code
      dto.birthday = p.birthday.toString
      dto.address = p.address
      dto.phone = p.phone
      dto
