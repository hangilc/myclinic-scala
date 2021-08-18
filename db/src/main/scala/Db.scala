package dev.myclinic.scala.db

import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.db.DbSqlite
import dev.myclinic.scala.db.DbPrim
import dev.myclinic.scala.model._
import cats.effect.IO
import cats._
import cats.implicits._
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._

object Db {
  val xa = DbSqlite.xa

  def query[A](sql: ConnectionIO[A]): IO[A] = {
    sql.transact(xa)
  }

  def listAppoint(from: LocalDate, upto: LocalDate): IO[List[Appoint]] = {
    query(DbPrim.listAppoint(from, upto))
  }

  def getAppoint(date: LocalDate, time: LocalTime): IO[Appoint] = {
    query(DbPrim.getAppoint(date, time))
  }

  def findAppoint(date: LocalDate, time: LocalTime): IO[Option[Appoint]] = {
    query(DbPrim.findAppoint(date, time))
  }

  def createAppointTimes(times: List[(LocalDate, LocalTime)]): IO[Unit] = {
    val ins = times.map(_ match {
      case (d, t) => DbPrim.enterAppoint(Appoint.create(d, t))
    })
    query(ins.sequence).map(_ => ())
  }

  def d(year: Int, month: Int, day: Int) = LocalDate.of(year, month, day)

  def t(hour: Int, minute: Int) = LocalTime.of(hour, minute, 0)

}