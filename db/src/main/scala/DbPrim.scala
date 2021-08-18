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
}