package dev.myclinic.scala.db

import dev.myclinic.scala.model.{Patient, AppEvent}
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler.jdkLogHandler
import dev.myclinic.scala.db.DoobieMapping._

object DbPatientPrim:
  def getPatient(patientId: Int): Query0[Patient] =
    sql"""
      select * from patient where patient_id = ${patientId}
    """.query[Patient]

  def searchPatient(text: String): Query0[Patient] =
    val t: String = s"%${text}%"
    sql""" 
      select * from patient where last_name like ${t}
        or first_name like ${t}
        or last_name_yomi like ${t}
        or first_name_yomi like ${t}
        order by last_name_yomi, first_name_yomi
    """.query[Patient]

  def searchPatient(lastPart: String, firstPart: String): Query0[Patient] =
    val t1: String = s"%${lastPart}%"
    val t2: String = s"%${firstPart}%"
    sql"""
      select * from patient where 
        (last_name like ${t1} or last_name_yomi like ${t1}) and 
        (first_name like ${t2} or first_name_yomi like ${t2})
        order by last_name_yomi, first_name_yomi
    """.query[Patient]

  def enterPatient(patient: Patient): ConnectionIO[AppEvent] =
    val op = sql"""
      insert into patient (last_name, first_name, last_name_yomi, first_name_yomi,
          sex, birth_day, address, phone) 
        values (${patient.lastName}, ${patient.firstName}, ${patient.lastNameYomi}, ${patient.firstNameYomi},
          ${patient.sex.code}, ${patient.birthday}, ${patient.address}, ${patient.phone})
    """
    for
      patientId <- op.update.withUniqueGeneratedKeys[Int]("patient_id")
      entered <- getPatient(patientId).unique
      event <- DbEventPrim.logPatientCreated(entered)
    yield event

  def updatePatient(patient: Patient): ConnectionIO[AppEvent] =
    val op = sql"""
      update patient set
        last_name = ${patient.lastName},
        first_name = ${patient.firstName},
        last_name_yomi = ${patient.lastNameYomi},
        first_name_yomi = ${patient.firstNameYomi},
        sex = ${patient.sex.code},
        birth_day = ${patient.birthday},
        address = ${patient.address},
        phone = ${patient.phone}
      where patient_id = ${patient.patientId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException("Update patient failed")
      updated <- getPatient(patient.patientId).unique
      event <- DbEventPrim.logPatientUpdated(updated)
    yield event

  def batchGetPatient(patientIds: List[Int]): ConnectionIO[Map[Int, Patient]] =
    for
      patients <- patientIds
        .map(patientId => getPatient(patientId).unique)
        .sequence
      items = patients.map(patient => (patient.patientId, patient))
    yield Map(items: _*)
