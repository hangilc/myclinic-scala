package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbConductKizaiPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbConductKizai extends Mysql:
  def countConductKiaziForConduct(conductId: Int): IO[Int] =
    mysql(sql"""
      select count(*) from visit_conduct_kiazi where conduct_id = ${conductId}
    """.query[Int].unique)

  def listConductKizaiForConduct(conductId: Int): IO[List[ConductKizai]] =
    mysql(Prim.listConductKizaiForConduct(conductId))

  def listConductKizaiIdForConduct(conductId: Int): IO[List[Int]] =
    mysql(Prim.listConductKizaiIdForConduct(conductId))

  def enterConductKizai(conductKizai: ConductKizai): IO[(AppEvent, ConductKizai)] =
    mysql(Prim.enterConductKizai(conductKizai))

  def deleteConductKizai(conductKizaiId: Int): IO[AppEvent] =
    mysql(Prim.deleteConductKizai(conductKizaiId))
