package dev.myclinic.scala.db

import java.time._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

object DbPrim {
  def listAppoint(from: LocalDate, upto: LocalDate): ConnectionIO[List[Appoint]] = {
    sql"select * from appoint where date >= $from and date <= $upto order by date, time"
      .query[Appoint].to[List]
  }

  def getAppoint(date: LocalDate, time: LocalTime): ConnectionIO[Appoint] = {
    sql"select * from appint where date = $date and time = $time"
      .query[Appoint].unique
  }

  def findAppoint(date: LocalDate, time: LocalTime): ConnectionIO[Option[Appoint]] = {
    sql"select * from appint where date = $date and time = $time"
      .query[Appoint].option
  }

  def enterAppoint(appoint: Appoint): ConnectionIO[Int] = {
    val patientId: Int = appoint.patientId.getOrElse(0)
    sql"""insert into appoint (date, time, patient_name, patient_id, memo) values 
      (${appoint.date}, ${appoint.time}, ${appoint.patientName}, $patientId, ${appoint.memo})""".update.run
  }
}