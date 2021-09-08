package dev.myclinic.scala.db

import cats.effect.IO
import cats.implicits._
import dev.myclinic.scala.model._
import doobie._
import doobie.implicits._

import java.time.LocalDate
import java.time.LocalTime

trait DbAppoint extends Sqlite {
  def getAppoint(date: LocalDate, time: LocalTime): IO[Appoint] = {
    sqlite(DbAppointPrim.getAppoint(date, time).unique)
  }

  def listAppoint(from: LocalDate, upto: LocalDate): IO[List[Appoint]] = {
    sqlite(DbAppointPrim.listAppoint(from, upto).to[List])
  }

  def createAppointTimes(
      times: List[(LocalDate, LocalTime)]
  )(implicit encoder: JsonEncoder): IO[Unit] = {
    def seq(eventId: Int): ConnectionIO[Unit] =
      times
        .map({ (d: LocalDate, t: LocalTime) =>
          {
            val app = Appoint(d, t, eventId, "", 0, "")
            DbAppointPrim.enterAppoint(app) *>
              AppEventHelper.enterAppointEvent(eventId, "created", app)
          }
        }.tupled)
        .sequence
        .void

    sqlite(DbEvent.withEventId(seq _))
  }

  def registerAppoint(a: Appoint)(implicit encoder: JsonEncoder): IO[Unit] = {
    require(!a.patientName.isEmpty)

    sqlite(
      DbEvent.withEventId(eventId =>
        for {
          cur <- DbAppointPrim.getAppoint(a.date, a.time).unique
          _ <- Helper.confirm(cur.isVacant, s"Appoint is not vacant: ${cur}")
          to = a.copy(eventId = eventId)
          _ <- DbAppointPrim.updateAppoint(to)
          _ <- AppEventHelper.enterAppointEvent(eventId, "updated", to)
        } yield ()
      )
    )
  }

  def cancelAppoint(
      date: LocalDate,
      time: LocalTime,
      patientName: String
  )(implicit encoder: JsonEncoder): IO[Unit] = {
    require(!patientName.isEmpty)

    sqlite(
      DbEvent.withEventId(eventId =>
        for {
          cur <- DbAppointPrim.getAppoint(date, time).unique
          _ <- Helper.confirm(!cur.isVacant, s"Appoint is vacant: ${cur}")
          _ <- Helper.confirm(
            cur.patientName == patientName,
            s"Inconsistent patient names: ${patientName} != ${cur.patientName}"
          )
          to = Appoint(date, time, eventId, "", 0, "")
          _ <- DbAppointPrim.updateAppoint(to)
          _ <- AppEventHelper.enterAppointEvent(eventId, "updated", to)
        } yield ()
      )
    )
  }

}
