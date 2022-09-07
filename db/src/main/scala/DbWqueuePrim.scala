package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*
import dev.myclinic.scala.db.DoobieMapping.{given}

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

object DbWqueuePrim:
  def getWqueue(visitId: Int): Query0[Wqueue] =
    sql"""
      select * from wqueue where visit_id = ${visitId}
    """.query[Wqueue]

  def deleteWqueue(visitId: Int): ConnectionIO[AppEvent] =
    val op = sql"""
      delete from wqueue where visit_id = ${visitId}
    """
    for
      wqueue <- getWqueue(visitId).unique
      affected <- op.update.run
      _ =
        if affected != 1 then
          throw new RuntimeException("Failed to delete wqueue.")
      event <- DbEventPrim.logWqueueDeleted(wqueue)
    yield event

  def tryDeleteWqueue(visitId: Int): ConnectionIO[Option[AppEvent]] =
    getWqueue(visitId).option.flatMap(optWqueue => {
      optWqueue match {
        case Some(wqueue) => deleteWqueue(visitId).map(Some(_))
        case None         => None.pure[ConnectionIO]
      }
    })

  def enterWqueue(wq: Wqueue): ConnectionIO[AppEvent] =
    val op = sql"""
      insert into wqueue (visit_id, wait_state) values (${wq.visitId}, ${wq.waitState.code})
    """
    for
      _ <- op.update.run
      entered <- getWqueue(wq.visitId).unique
      event <- DbEventPrim.logWqueueCreated(entered)
    yield event

  def updateWqueue(wq: Wqueue): ConnectionIO[AppEvent] =
    val op = sql"""
      update wqueue set wait_state = ${wq.waitState.code} where visit_id = ${wq.visitId}
    """
    for
      affected <- op.update.run
      _ = if affected != 1 then throw new RuntimeException(s"Failed to update wqueue: ${wq}")
      event <- DbEventPrim.logWqueueUpdated(wq)
    yield event

  def tryChangeWqueueState(visitId: Int, newWaitState: WaitState): ConnectionIO[Option[(AppEvent, Wqueue)]] =
    for
      wqOpt <- getWqueue(visitId).option
      result <- wqOpt.fold(None.pure[ConnectionIO])(wq => changeWqueueState(wq, newWaitState).map(Some(_)))
    yield ???

  private def changeWqueueState(wq: Wqueue, newWaitState: WaitState): ConnectionIO[(AppEvent, Wqueue)] =
    val newWq = wq.copy(waitState = newWaitState)
    for
      event <- updateWqueue(newWq)
    yield (event, newWq)

  def changeWqueueState(visitId: Int, newWaitState: WaitState): ConnectionIO[(AppEvent, Wqueue)] =
    for
      wq <- getWqueue(visitId).unique
      result <- changeWqueueState(wq, newWaitState)
    yield result

  def listWqueue(): Query0[Wqueue] =
    sql"""
      select visit_id, wait_state from wqueue order by visit_id
    """
      .query[(Int, Int)]
      .map(tuple =>
        tuple match {
          case (visitId, waitStateCode) => {
            val waitState = WaitState.fromCode(waitStateCode)
            Wqueue(visitId, waitState)
          }
        }
      )
