package dev.myclinic.scala.db

import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.db.DbSqlite
import dev.myclinic.scala.db.DbPrim
import cats.effect.IO
import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._

object Db {
  val xa = DbSqlite.xa

  def exec[A](sql: ConnectionIO[A]): IO[A] = {
    sql.transact(xa)
  }

  def listAppoint(from: LocalDate, upto: LocalDate): IO[List[Appoint]] = {
    exec(DbPrim.listAppoint(from, upto))
  }
}