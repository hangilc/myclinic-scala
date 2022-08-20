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

  extension (p: Patient)
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

  private def validUptoToString(validUpto: ValidUpto): String =
    validUpto.value match {
      case Some(d) => d.toString
      case None => "0000-00-00"
    }

  extension (m: Shahokokuho)
    def toDTO: ShahokokuhoDTO =
      val dto = new ShahokokuhoDTO()
      dto.shahokokuhoId = m.shahokokuhoId
      dto.patientId = m.patientId
      dto.hokenshaBangou = m.hokenshaBangou
      dto.hihokenshaKigou = m.hihokenshaKigou
      dto.hihokenshaBangou = m.hihokenshaBangou
      dto.honnin = m.honninStore
      dto.kourei = m.koureiStore
      dto.validFrom = m.validFrom.toString
      dto.validUpto = validUptoToString(m.validUpto)
      dto.edaban = m.edaban
      dto

  extension (m: Koukikourei)
    def toDTO: KoukikoureiDTO =
      val dto = new KoukikoureiDTO()
      dto.koukikoureiId = m.koukikoureiId
      dto.patientId = m.patientId
      dto.hokenshaBangou = m.hokenshaBangou
      dto.hihokenshaBangou = m.hihokenshaBangou
      dto.futanWari = m.futanWari
      dto.validFrom = m.validFrom.toString
      dto.validUpto = validUptoToString(m.validUpto)
      dto

  extension (m: Roujin)
    def toDTO: RoujinDTO =
      val dto = new RoujinDTO()
      dto.roujinId = m.roujinId
      dto.patientId = m.patientId
      dto.shichouson = m.shichouson
      dto.jukyuusha = m.jukyuusha
      dto.futanWari = m.futanWari
      dto.validFrom = m.validFrom.toString
      dto.validUpto = validUptoToString(m.validUpto)
      dto

  extension (m: Kouhi)
    def toDTO: KouhiDTO =
      val dto = new KouhiDTO()
      dto.kouhiId = m.kouhiId
      dto.patientId = m.patientId
      dto.futansha = m.futansha
      dto.jukyuusha = m.jukyuusha
      dto.validFrom = m.validFrom.toString
      dto.validUpto = validUptoToString(m.validUpto)
      dto