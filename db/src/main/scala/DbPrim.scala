package dev.myclinic.scala.db

import java.time._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

object DbPrim extends AppointPrim with AppEventPrim {}

private object Helper {
  def confirmUpdate(affected: Int, err: => String) = {
    if (affected != 1) {
      throw new RuntimeException(err)
    }
    ().pure[ConnectionIO]
  }
}

trait AppointPrim {
  def enterAppoint(appoint: Appoint): ConnectionIO[Unit] = {
    (sql"""insert into appoint (date, time, event_id, patient_name, patient_id, memo) values 
      (${appoint.date}, ${appoint.time}, ${appoint.eventId}, ${appoint.patientName}, 
      ${appoint.patientId}, ${appoint.memo})""".update.run) flatMap {
      affected =>
        Helper.confirmUpdate(affected, s"Failed to enter appoint: ${appoint}")
    }
  }

  def updateAppoint(a: Appoint): ConnectionIO[Unit] = {
    sql"""
      update appoint set event_id = ${a.eventId}, 
        patient_name = ${a.patientName}, patient_id = ${a.patientId},
        memo = ${a.memo} 
        where date = ${a.date} and time = ${a.time}
    """.update.run flatMap { affected =>
      Helper.confirmUpdate(affected, s"Failed to update appoint: ${a}")
    }
  }

  def deleteAppoint(date: LocalDate, time: LocalTime): ConnectionIO[Unit] = {
    sql"delete from appoint where date = ${date} and time = ${time}".update.run flatMap {
      affected =>
        Helper.confirmUpdate(
          affected,
          s"Failed to delete appoint: ${date} ${time}"
        )
    }
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
  def getAppEvent(id: Int): Query0[AppEvent] = {
    sql"select * from app_event where id = ${id}".query[AppEvent]
  }

  def enterAppEvent(
      eventId: Int,
      model: String,
      kind: String,
      data: String
  ): ConnectionIO[AppEvent] = {
    val createdAt = LocalDateTime.now()
    for {
      id <- sql"""
          insert into app_event (created_at, event_id, model, kind, data) values (
            ${createdAt}, ${eventId}, ${model}, ${kind}, ${data}
          )
        """.update.withUniqueGeneratedKeys[Int]("id")
      entered <- getAppEvent(id).unique
    } yield entered
  }

  def setEventId(eventId: Int): ConnectionIO[Unit] = {
    def confirm1(affected: Int): Unit = {
      if (affected != 1) {
        throw new RuntimeException("Failed to update event_id")
      }
    }

    for {
      affected <- sql"""
          update event_id_store set event_id = ${eventId} where id = 0
        """.update.run
      _ = confirm1(affected)
    } yield ()
  }

  def getCurrentEventId(): ConnectionIO[Option[Int]] = {
    sql"""
      select id from event_id_store order by id desc limit 1
    """.query[Int].option
  }

  def getNextEventId(): ConnectionIO[Int] = {
    def next(curr: Option[Int]): Int = curr match {
      case Some(v) => v + 1
      case None    => 0
    }

    for {
      currOpt <- getCurrentEventId()
    } yield next(currOpt)
  }

}
