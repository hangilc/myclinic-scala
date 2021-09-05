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
      encode: Events.FromTo[Appoint] => String
  ): IO[AppEvent] = {
    def confirmVacant(app: Appoint): ConnectionIO[Unit] = {
      if (!app.isVacant) {
        throw new RuntimeException(s"Appoint slot is not empty: $date $time")
      }
      ().pure[ConnectionIO]
    }

    def update(): ConnectionIO[Appoint] = {
      val app = Appoint(date, time, patientName, 0, "")
      def confirm1(i: Int): ConnectionIO[Unit] = i match {
        case 1 => ().pure[ConnectionIO]
        case _ => throw new RuntimeException(s"Failed to update appoint ${app}")
      }
      for {
        affected <- DbPrim.updateAppoint(app).run
        _ <- confirm1(affected)
      } yield app
    }

    require(!patientName.isEmpty)
    val ops = for {
      at <- DbPrim.getAppoint(date, time).unique
      _ <- confirmVacant(at)
      to <- update()
      appEvent <- DbPrim.enterAppEvent(
        "appoint",
        "updated",
        encode(Events.FromTo[Appoint](at, to))
      )
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
      app <- DbPrim.getAppoint(date, time).unique
      _ <- confirmName(app)
      _ <- {
        val update = Appoint.create(date, time)
        DbPrim.updateAppoint(update).run
      }
    } yield ())
    exec(ops)
  }

  def getNextAppEventId(): IO[Int] = {
    val ops =
      for (idOpt <- DbPrim.getNextAppEventId().option) yield (idOpt match {
        case Some(id) => id
        case None     => 0
      })
    exec(ops)
  }

}
