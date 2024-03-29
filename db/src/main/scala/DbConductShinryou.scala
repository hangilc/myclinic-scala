package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbConductShinryouPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbConductShinryou extends Mysql:
  def countConductShinryouForConduct(conductId: Int): IO[Int] =
    mysql(sql"""
      select count(*) from visit_conduct_shinryou where conduct_id = ${conductId}
    """.query[Int].unique)

  def listConductShinryouForConduct(conductId: Int): IO[List[ConductShinryou]] =
    mysql(Prim.listConductShinryouForConduct(conductId))

  def listConductShinryouIdForConduct(conductId: Int): IO[List[Int]] =
    mysql(Prim.listConductShinryouIdForConduct(conductId))

  def enterConductShinryou(conductShinryou: ConductShinryou): IO[(AppEvent, ConductShinryou)] =
    mysql(Prim.enterConductShinryou(conductShinryou))

  def updateConductShinryou(conductShinryou: ConductShinryou): IO[(AppEvent, ConductShinryou)] =
    mysql(Prim.updateConductShinryou(conductShinryou))

  def deleteConductShinryou(conductShinryouId: Int): IO[AppEvent] =
    mysql(Prim.deleteConductShinryou(conductShinryouId))