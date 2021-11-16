package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DbConductDrugPrim:
  def listConductDrugForConduct(conductId: Int): ConnectionIO[List[ConductDrug]] =
    sql"""
      select * from visit_conduct_drug where conduct_id = ${conductId} 
      order by conduct_drug_id
    """.query[ConductDrug].to[List]