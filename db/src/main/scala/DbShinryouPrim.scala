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

object DbShinryouPrim:
  def countShinryouForVisit(visitId: Int): ConnectionIO[Int] =
    sql"""
      select count(*) from visit_shinryou where visit_id = ${visitId}
    """.query[Int].unique

  def listShinryouForVisit(visitId: Int): ConnectionIO[List[Shinryou]] =
    sql"""
      select * from visit_shinryou where visit_id = ${visitId} order by shinryou_id
    """.query[Shinryou].to[List]

  def listShinryouIdForVisit(visitId: Int): ConnectionIO[List[Int]] =
    sql"""
      select shinryou_id from visit_shinryou where visit_id = ${visitId} order by shinryou_id
    """.query[Int].to[List]

  def getShinryou(shinryouId: Int): Query0[Shinryou] =
    sql"""
      select * from visit_shinryou where shinryou_id = ${shinryouId}
    """.query[Shinryou]

  def enterShinryou(shinryou: Shinryou): ConnectionIO[(AppEvent, Shinryou)] =
    val op = sql"""
      insert into visit_shinryou set visit_id = ${shinryou.visitId}, 
        shinryoucode = ${shinryou.shinryoucode}
    """
    for
      visit <- DbVisitPrim.getVisit(shinryou.visitId).unique
      _ <- DbShinryouMasterPrim
        .getShinryouMaster(shinryou.shinryoucode, visit.visitedAt.toLocalDate)
        .unique
      shinryouId <- op.update.withUniqueGeneratedKeys[Int]("shinryou_id")
      entered <- getShinryou(shinryouId).unique
      event <- DbEventPrim.logShinryouCreated(entered)
    yield (event, entered)

  def deleteShinryou(shinryouId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from visit_shinryou where shinryou_id = ${shinryouId}
    """
    for
      shinryou <- getShinryou(shinryouId).unique
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to delete shinryou: ${shinryouId}")
      event <- DbEventPrim.logShinryouDeleted(shinryou)
    yield event


