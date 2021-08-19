package dev.myclinic.scala.db

import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.model._
import cats.effect.IO
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._

trait DbExecutor {
  def exec[A](s: ConnectionIO[A]): IO[A]
  def execVoid(s: ConnectionIO[_]): IO[Unit] = exec(s).map(_ => ())
}

object Db extends DbExecutor with DbAppoint {
  val xa = DbSqlite.xa

  override def exec[A](s: ConnectionIO[A]): IO[A] = s.transact(xa)

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
    exec(ins.sequence).map(_ => ())
  }

  def listAppoint(from: LocalDate, upto: LocalDate): IO[List[Appoint]] = {
    exec(DbPrim.listAppoint(from, upto).to[List])
  }

  def registerAppoint(
      date: LocalDate,
      time: LocalTime,
      patientName: String
  ): IO[Unit] = {
    require(!patientName.isEmpty)
    execVoid(for(
      at <- DbPrim.getAppoint(date, time).unique;
      _ <- {
        if( !at.patientName.isEmpty ){
          throw new RuntimeException(s"Appointment already exists: ${at}")
        } else {
          val a = Appoint(at.date, at.time, patientName, 0, "")
          DbPrim.updateAppoint(a).run
        }
      }
    ) yield())
  }

  def cancelAppoint(date: LocalDate, time: LocalTime, patientName: String): IO[Unit] = {
    require(!patientName.isEmpty)
    execVoid(
      for(
        at <- DbPrim.getAppoint(date, time).unique;
        _ <- {
          if( at.patientName != patientName ){
            throw new RuntimeException(s"Inconsistent appoiont: ${at}")
          } else {
            val a = Appoint.create(date, time)
            DbPrim.updateAppoint(a).run
          }
        }
      ) yield ()
    )
  }

}
