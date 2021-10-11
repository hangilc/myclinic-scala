package dev.myclinic.scala.db

import java.time._
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model.{AppointTime, Appoint, AppEvent}
import dev.myclinic.scala.db.DoobieMapping._

object DbAppointPrim:

  def getAppointTime(appointTimeId: Int): Query0[AppointTime] =
    sql"""
      select * from appoint_time where appoint_time_id = ${appointTimeId}
    """.query[AppointTime]

  def enterAppointTime(at: AppointTime): ConnectionIO[AppointTime] =
    val op = sql"""
      insert into appoint_time (event_id, date, from_time, until_time, 
        kind, capacity)
        values(${at.eventId}, ${at.date}, ${at.fromTime}, ${at.untilTime}, 
        ${at.kind}, ${at.capacity})
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("appoint_time_id")
      entered <- getAppointTime(id).unique
    yield entered

  def updateAppointTime(at: AppointTime): ConnectionIO[AppointTime] =
    val op = sql"""
      update appoint_time set event_id = ${at.eventId}, date = ${at.date},
        from_time = ${at.fromTime}, until_time = ${at.untilTime}, 
        kind = ${at.kind}, capacity = ${at.capacity}
        where appoint_time_id = ${at.appointTimeId}
    """
    
    for
      affected <- op.update.run
      _ = assert(affected == 1, "Failed to update appoint time.")
      updated <- getAppointTime(at.appointTimeId).unique
    yield updated

  def deleteAppointTime(appointTimeId: Int): ConnectionIO[Unit] =
    sql"""
      delete from appoint_time where appoint_time_id = ${appointTimeId}
    """.update.run.map[Unit](affected =>
      if affected != 1 then
        throw new RuntimeException("Failed to delete appoint time.")
    )

  def listExistingAppointTimeDates(
      from: LocalDate,
      upto: LocalDate
  ): Query0[LocalDate] =
    sql"""
      select distinct date from appoint_time 
        where date >= ${from} and date <= ${upto}
    """.query[LocalDate]

  def listAppointTimes(
      from: LocalDate,
      upto: LocalDate
  ): Query0[AppointTime] =
    sql"""
      select * from appoint_time where date >= ${from} and
        date <= ${upto} order by date, from_time
    """.query[AppointTime]

  def listAppointTimes(
      date: LocalDate,
      from: LocalTime,
      upto: LocalTime
  ): Query0[AppointTime] =
    sql"""
      select * from appoint_time where date = ${date} and
        fromTime >= ${from} and fromTime <= ${upto}
        order by date, from_time
    """.query[AppointTime]

  def listAppointTimesForDate(date: LocalDate): Query0[AppointTime] =
    sql"""
      select * from appoint_time where date = ${date}
        order by date, from_time
    """.query[AppointTime]

  def getAppoint(appointId: Int): Query0[Appoint] =
    sql"select * from appoint where appoint_id = ${appointId}".query[Appoint]

  def enterAppoint(a: Appoint): ConnectionIO[Appoint] =
    val op = sql"""
      insert into appoint (event_id, appoint_time_id, patient_name, 
        patient_id, memo) 
        values (${a.eventId}, ${a.appointTimeId}, ${a.patientName}, 
        ${a.patientId}, ${a.memo})
      """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("appoint_id")
      entered <- getAppoint(id).unique
    yield entered

  def updateAppoint(a: Appoint): ConnectionIO[Appoint] =
    val op = sql"""
      update appoint set event_id = ${a.eventId}, 
        appoint_time_id = ${a.appointTimeId},
        patient_name = ${a.patientName}, 
        patient_id = ${a.patientId},
        memo = ${a.memo} 
        where appoint_id = ${a.appointId}
    """
    
    for 
      affected <- op.update.run
      _ = assert(affected == 1, "Failed to update appoint.")
      updated <- getAppoint(a.appointId).unique
    yield updated

  def deleteAppoint(appointId: Int): ConnectionIO[Unit] =
    sql"delete from appoint where appoint_id = ${appointId}".update.run.map(
      affected =>
        if affected != 1 then
          throw new RuntimeException("Failed to delete appoint.")
    )

  def listAppointsForAppointTime(appointTimeId: Int): Query0[Appoint] =
    sql"""
      select * from appoint where appoint_time_id = ${appointTimeId}
        order by appoint_id
    """.query[Appoint]

  def listAppointsForDate(date: LocalDate): Query0[Appoint] =
    sql"""
      select a.* from appoint as a inner join appoint_time as at
        on a.appoint_time_id = at.appoint_time_id 
        where at.date = ${date}
    """.query[Appoint]

  def countAppointsByAppointTime(appointTimeId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from appoint where appoint_time_id = ${appointTimeId}
    """.query[Int].unique