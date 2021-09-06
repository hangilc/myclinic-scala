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
    for{
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
    val ins = times.map(_ match {
      case (d, t) => DbPrim.enterAppoint(Appoint.create(d, t)).run
    })
    exec(ins.sequence).void
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
      patientName: String,
      encode: Appoint => String
  ): IO[AppEvent] = {
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
    val ops = for {
      eventId <- DbPrim.getNextEventId()
      at <- DbPrim.getAppoint(date, time).unique
      _ = confirmVacant(at)
      to <- update(eventId)
      appEvent <- DbPrim.enterAppEvent(
        "appoint",
        "updated",
        encode(to)
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
    def confirmName(app: Appoint): ConnectionIO[Unit] = {
      if (patientName != app.patientName) {
        throw new RuntimeException(s"Inconsistent patient name: $patientName")
      }
      ().pure[ConnectionIO]
    }

    require(!patientName.isEmpty)
    val ops = (for {
      eventId <- DbPrim.getNextEventId()
      app <- DbPrim.getAppoint(date, time).unique
      _ <- confirmName(app)
      _ <- {
        val update = Appoint.create(date, time)
        DbPrim.updateAppoint(update).run
      }
    } yield ())
    exec(ops)
  }

  def getNextEventId(): IO[Int] = {
    exec(DbPrim.getNextEventId())
  }

  def setEventId(eventId: Int): IO[Unit] = {
    def confirm1(affected: Int): IO[Unit] = {
      if( affected != 1 ){
        throw new RuntimeException("Failed to update event_id.")
      } else {
        ().pure[IO]
      }
    }

    for{
      affected <- DbPrim.setCurrentEventId(eventId).run
      _ <- confirm1(affected)
    } yield ()
  }

}
