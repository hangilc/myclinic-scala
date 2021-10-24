package dev.myclinic.scala.db

import dev.myclinic.scala.model.Patient
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