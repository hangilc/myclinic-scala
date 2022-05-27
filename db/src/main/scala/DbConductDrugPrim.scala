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

object DbConductDrugPrim:
  private val tConductDrug = Fragment.const("visit_conduct_drug")
  private val cConductDrugId = Fragment.const("id")
  private val cConductId = Fragment.const("visit_conduct_id")
  def listConductDrugForConduct(conductId: Int): ConnectionIO[List[ConductDrug]] =
    sql"""
      select * from $tConductDrug where $cConductId = ${conductId} 
      order by $cConductDrugId
    """.query[ConductDrug].to[List]

  def listConductDrugIdForConduct(conductId: Int): ConnectionIO[List[Int]] =
    sql"""
      select $cConductDrugId from $tConductDrug where $cConductId = ${conductId} 
      order by $cConductDrugId
    """.query[Int].to[List]

  def getConductDrug(conductDrugId: Int): Query0[ConductDrug] =
    sql"""
      select * from visit_conduct_drug where id = ${conductDrugId}
    """.query[ConductDrug]

  def enterConductDrug(cd: ConductDrug): ConnectionIO[(AppEvent, ConductDrug)] =
    val op = sql"""
      insert into visit_conduct_drug set visit_conduct_id = ${cd.conductId},
        iyakuhincode = ${cd.iyakuhincode}, amount = ${cd.amount}
    """
    for
      id <- op.update.withUniqueGeneratedKeys[Int]("id")
      entered <- getConductDrug(id).unique
      event <- DbEventPrim.logConductDrugCreated(entered)
    yield (event, entered)
