package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbDrugPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbDrug extends Mysql:
  def countDrugForVisit(visitId: Int): IO[Int] =
    mysql(sql"""
      select count(*) from visit_drug where visit_id = ${visitId}
    """.query[Int].unique)

  def listDrugForVisit(visitId: Int): IO[List[Drug]] =
    mysql(Prim.listDrugForVisit(visitId))