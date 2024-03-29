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

object DbConductShinryouPrim:
  private val tConductShinryou = Fragment.const("visit_conduct_shinryou")
  private val cConductId = Fragment.const("visit_conduct_id")
  private val cConductShinryouId = Fragment.const("id")

  def listConductShinryouForConduct(conductId: Int): ConnectionIO[List[ConductShinryou]] =
    sql"""
      select * from $tConductShinryou where $cConductId = ${conductId} 
        order by $cConductShinryouId
    """.query[ConductShinryou].to[List]
    
  def listConductShinryouIdForConduct(conductId: Int): ConnectionIO[List[Int]] =
    sql"""
      select $cConductShinryouId from $tConductShinryou where $cConductId = ${conductId} 
        order by $cConductShinryouId
    """.query[Int].to[List]

  def getConductShinryou(conductShinryouId: Int): Query0[ConductShinryou] =
    sql"""
      select * from visit_conduct_shinryou where id = ${conductShinryouId}
    """.query[ConductShinryou]

  def enterConductShinryou(cs: ConductShinryou): ConnectionIO[(AppEvent, ConductShinryou)] =
    val op = sql"""
      insert into visit_conduct_shinryou set visit_conduct_id = ${cs.conductId},
        shinryoucode = ${cs.shinryoucode},
        memo = ${cs.memo}
    """
    for 
      conduct <- DbConductPrim.getConduct(cs.conductId).unique
      visit <- DbVisitPrim.getVisit(conduct.visitId).unique
      _ <- DbShinryouMasterPrim.getShinryouMaster(cs.shinryoucode, visit.visitedDate).unique
      id <- op.update.withUniqueGeneratedKeys[Int]("id")
      entered <- getConductShinryou(id).unique
      event <- DbEventPrim.logConductShinryouCreated(entered)
    yield (event, entered)

  def updateConductShinryou(cs: ConductShinryou): ConnectionIO[(AppEvent, ConductShinryou)] =
    val op = sql"""
      update visit_conduct_shinryou set
        shinryoucode = ${cs.shinryoucode},
        memo = ${cs.memo}
      where id = ${cs.conductShinryouId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException("update conduct shinryou failed")
      updated <- getConductShinryou(cs.conductShinryouId).unique
      event <- DbEventPrim.logConductShinryouUpdated(updated)
    yield (event, updated)

  def deleteConductShinryou(conductShinryouId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from visit_conduct_shinryou where id = ${conductShinryouId}
    """
    for
      shinryou <- getConductShinryou(conductShinryouId).unique
      affected <- op.update.run
      _ = if affected != 1 then
        throw new RuntimeException(
          s"Failed to delete conduct shinryou: ${conductShinryouId}"
        )
      event <- DbEventPrim.logConductShinryouDeleted(shinryou)
    yield event


