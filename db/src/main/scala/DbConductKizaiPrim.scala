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

object DbConductKizaiPrim:
  private val tConductKizai = Fragment.const("visit_conduct_kizai")
  private val cConductKizaiId = Fragment.const("id")
  private val cConductId = Fragment.const("visit_conduct_id")
  def listConductKizaiForConduct(conductId: Int): ConnectionIO[List[ConductKizai]] =
    sql"""
      select * from $tConductKizai where $cConductId = ${conductId} 
        order by $cConductKizaiId
    """.query[ConductKizai].to[List]
    
  def listConductKizaiIdForConduct(conductId: Int): ConnectionIO[List[Int]] =
    sql"""
      select $cConductKizaiId from $tConductKizai where $cConductId = ${conductId} 
        order by $cConductKizaiId
    """.query[Int].to[List]

  def getConductKizai(conductKizaiId: Int): Query0[ConductKizai] =
    sql"""
      select * from visit_conduct_kizai where id = ${conductKizaiId}
    """.query[ConductKizai]

  def enterConductKizai(ck: ConductKizai): ConnectionIO[(AppEvent, ConductKizai)] =
    val op = sql"""
      insert into visit_conduct_kizai set visit_conduct_id = ${ck.conductId},
        kizaicode = ${ck.kizaicode}, amount = ${ck.amount}
    """
    for
      conduct <- DbConductPrim.getConduct(ck.conductId).unique
      visit <- DbVisitPrim.getVisit(conduct.visitId).unique
      _ <- DbKizaiMasterPrim.getKizaiMaster(ck.kizaicode, visit.visitedDate).unique
      id <- op.update.withUniqueGeneratedKeys[Int]("id")
      entered <- getConductKizai(id).unique
      event <- DbEventPrim.logConductKizaiCreated(entered)
    yield (event, entered)

