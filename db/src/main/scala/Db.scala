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
      patientName: String
  ): IO[Unit] = {
    def confirmVacant(app: Appoint): ConnectionIO[Unit] = {
      if (!app.isVacant) {
        throw new RuntimeException(s"Appoint slot is not empty: $date $time")
      }
      ().pure[ConnectionIO]
    }

    def register(): ConnectionIO[Int] = {
      val app = Appoint(date, time, patientName, 0, "")
      DbPrim.updateAppoint(app).run
    }

    require(!patientName.isEmpty)
    val ops = for {
      at <- DbPrim.getAppoint(date, time).unique
      _ <- confirmVacant(at)
      _ <- register()
    } yield ()
    exec(ops)
  }

  // def cancelAppoint(
  //     date: LocalDate,
  //     time: LocalTime,
  //     patientName: String
  // ): IO[Unit] = {
  //   require(!patientName.isEmpty)
  //   execVoid(
  //     for (
  //       at <- DbPrim.getAppoint(date, time).unique;
  //       _ <- {
  //         if (at.patientName != patientName) {
  //           throw new RuntimeException(s"Inconsistent appoiont: ${at}")
  //         } else {
  //           val a = Appoint.create(date, time)
  //           DbPrim.updateAppoint(a).run
  //         }
  //       }
  //     ) yield ()
  //   )
  // }

}
