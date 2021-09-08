package dev.myclinic.scala.db

import java.time._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

object DbAppointPrim {

  def enterAppoint(a: Appoint): ConnectionIO[Unit] = {
    val op = sql"""
      insert into appoint (date, time, event_id, patient_name, patient_id, memo) 
        values (${a.date}, ${a.time}, ${a.eventId}, 
        ${a.patientName}, ${a.patientId}, ${a.memo})
      """.update.run
    op >>= Helper.confirmUpdate(s"Failed to enter appoint: ${a}.")
  }

  def updateAppoint(a: Appoint): ConnectionIO[Unit] = {
    val op = sql"""
      update appoint set event_id = ${a.eventId}, 
        patient_name = ${a.patientName}, patient_id = ${a.patientId},
        memo = ${a.memo} 
        where date = ${a.date} and time = ${a.time}
    """.update.run
    op >>= Helper.confirmUpdate(s"Failed to update appoint: ${a}")
  }

  def deleteAppoint(date: LocalDate, time: LocalTime): ConnectionIO[Unit] = {
    val op =
      sql"delete from appoint where date = ${date} and time = ${time}".update.run
    op >>= Helper.confirmUpdate(s"Failed to delete appoint")
  }

  def listAppoint(from: LocalDate, upto: LocalDate): Query0[Appoint] = {
    sql""""
      select * from appoint where date >= $from and date <= $upto order by date, time
    """.query[Appoint]
  }

  def getAppoint(date: LocalDate, time: LocalTime): Query0[Appoint] = {
    sql"select * from appoint where date = $date and time = $time"
      .query[Appoint]
  }

}
