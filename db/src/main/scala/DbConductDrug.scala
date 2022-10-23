package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbConductDrugPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbConductDrug extends Mysql:
  def countConductDrugForConduct(conductId: Int): IO[Int] =
    mysql(sql"""
      select count(*) from visit_conduct_drug where conduct_id = ${conductId}
    """.query[Int].unique)

  def listConductDrugForConduct(conductId: Int): IO[List[ConductDrug]] =
    mysql(Prim.listConductDrugForConduct(conductId))

  def listConductDrugIdForConduct(conductId: Int): IO[List[Int]] =
    mysql(Prim.listConductDrugIdForConduct(conductId))

  def enterConductDrug(conductDrug: ConductDrug): IO[(AppEvent, ConductDrug)] =
    mysql(Prim.enterConductDrug(conductDrug))

  def deleteConductDrug(conductDrugId: Int): IO[AppEvent] =
    mysql(Prim.deleteConductDrug(conductDrugId))

