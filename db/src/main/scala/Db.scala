package dev.myclinic.scala.db

import cats.effect.IO
import cats.implicits._
import dev.myclinic.scala.model._
import doobie._
import doobie.implicits._

import java.time.LocalDate
import java.time.LocalTime

trait DbExecutor {
  def exec[A](s: ConnectionIO[A]): IO[A]

  def update1[A](s: ConnectionIO[Int], err: => String): ConnectionIO[Unit] = {
    for {
      affected <- s
      _ = affected match {
        case 1 =>
        case _ => throw new RuntimeException(err)
      }
    } yield ()
  }
}

object Db extends DbExecutor with DbAppoint {
  val xa = DbSqlite.xa

  override def exec[A](s: ConnectionIO[A]): IO[A] = DbSqlite.exec(s)

}

trait DbAppoint extends DbExecutor {

  def getAppoint(date: LocalDate, time: LocalTime): IO[Appoint] = {
    exec(DbPrim.getAppoint(date, time).unique)
  }

  def getAppointOption(
      date: LocalDate,
      time: LocalTime
  ): IO[Option[Appoint]] = {
    exec(DbPrim.getAppoint(date, time).option)
  }

  def createAppointTimes(times: List[(LocalDate, LocalTime)]): IO[Unit] = {
    def seq(eventId: Int): ConnectionIO[Unit] = times.map({ (d: LocalDate, t: LocalTime) =>
      {
        val app = Appoint(d, t, eventId, "", 0, "")
        DbPrim.enterAppoint(app)
      }
    }.tupled).sequence.void

    exec(for {
      eventId <- DbPrim.getNextEventId()
      _ <- seq(eventId)
    } yield ())
  }

  def createAppointTimes(date: LocalDate, times: (Int, Int)*): IO[Unit] = {
    val items = times.map(x => (date, LocalTime.of(x._1, x._2, 0)))
    createAppointTimes(items.toList)
  }

  def listAppoint(from: LocalDate, upto: LocalDate): IO[List[Appoint]] = {
    exec(DbPrim.listAppoint(from, upto).to[List])
  }

  def registerAppoint(
      date: LocalDate,
      time: LocalTime,
      patientName: String
  )(implicit codec: ModelJsonCodec): IO[AppEvent] = {
    def confirmVacant(app: Appoint): Unit = {
      if (!app.isVacant) {
        throw new RuntimeException(s"Appoint slot is not empty: $date $time")
      }
    }

    def update(eventId: Int): ConnectionIO[Appoint] = {
      val app = Appoint(date, time, eventId, patientName, 0, "")
      DbPrim.updateAppoint(app) >> app.pure[ConnectionIO]
    }

    require(!patientName.isEmpty)
    val ops: ConnectionIO[AppEvent] = for {
      eventId <- DbPrim.getNextEventId()
      at <- DbPrim.getAppoint(date, time).unique
      _ = confirmVacant(at)
      to <- update(eventId)
      appEvent <- DbPrim.enterAppEvent(
        eventId,
        "appoint",
        "updated",
        codec.encodeAppoint(to)
      )
      _ <- DbPrim.setEventId(eventId)
    } yield appEvent
    exec(ops)
  }

  def cancelAppoint(
      date: LocalDate,
      time: LocalTime,
      patientName: String
  ): IO[Unit] = {
    require(!patientName.isEmpty)

    def confirmName(app: Appoint): ConnectionIO[Unit] = {
      if (patientName != app.patientName) {
        throw new RuntimeException(s"Inconsistent patient name: $patientName")
      }
      ().pure[ConnectionIO]
    }

    val ops: ConnectionIO[Unit] = (for {
      eventId <- DbPrim.getNextEventId()
      app <- DbPrim.getAppoint(date, time).unique
      _ <- confirmName(app)
      _ <- {
        val update = Appoint(date, time, eventId, "", 0, "")
        DbPrim.updateAppoint(update)
      }
      _ <- DbPrim.setEventId(eventId)
    } yield ())
    exec(ops)
  }

  def getNextEventId(): IO[Int] = {
    exec(DbPrim.getNextEventId())
  }

}
