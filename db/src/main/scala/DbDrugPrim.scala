package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DbDrugPrim:
  def countDrugForVisit(visitId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit_drug where visit_id = ${visitId}
    """.query[Int].unique

  def listDrugForVisit(visitId: Int): ConnectionIO[List[Drug]] =
    sql"""
      select * from visit_drug where visit_id = ${visitId} order by drug_id
    """.query[Drug].to[List]  

  def listDrugIdForVisit(visitId: Int): ConnectionIO[List[Int]] =
    sql"""
      select drug_id from visit_drug where visit_id = ${visitId} order by drug_id
    """.query[Int].to[List]