package dev.myclinic.scala.db

import java.time._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

object DbPrim extends AppointPrim with AppEventPrim {}

trait AppointPrim {
  def enterAppoint(appoint: Appoint): Update0 = {
    sql"""insert into appoint (date, time, patient_name, patient_id, memo) values 
      (${appoint.date}, ${appoint.time}, ${appoint.patientName}, 
      ${appoint.patientId}, ${appoint.memo})""".update
  }

  def updateAppoint(a: Appoint): Update0 = {
    sql"""
      update appoint set patient_name = ${a.patientName}, patient_id = ${a.patientId},
      memo = ${a.memo} where date = ${a.date} and time = ${a.time}
    """.update
  }

  def deleteAppoint(date: LocalDate, time: LocalTime): Update0 = {
    sql"delete from appoint where date = ${date} and time = ${time}".update
  }

  def listAppoint(from: LocalDate, upto: LocalDate): Query0[Appoint] = {
    sql"select * from appoint where date >= $from and date <= $upto order by date, time"
      .query[Appoint]
  }

  def getAppoint(date: LocalDate, time: LocalTime): Query0[Appoint] = {
    sql"select * from appoint where date = $date and time = $time"
      .query[Appoint]
  }
}

trait AppEventPrim {
  def enterAppEvent(
      model: String,
      kind: String,
      data: String
  ): ConnectionIO[AppEvent] = {
    println("enterAppEvent", model, kind, data)
    val createdAt = LocalDateTime.now()
    for {
      id <- sql"""
          insert into app_event (created_at, model, kind, data) values (
            ${createdAt}, ${model}, ${kind}, ${data}
          )
        """.update.withUniqueGeneratedKeys[Int]("id")
    } yield AppEvent(id, createdAt, model, kind, data)
  }

  def getNextAppEventId(): Query0[Int] = {
    sql"""
      select id from app_event order by id desc limit 1
    """.query[Int]
  }

}
