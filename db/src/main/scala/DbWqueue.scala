package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbWqueue extends Mysql:
  def listWqueue(): IO[List[Wqueue]] =
    mysql(DbWqueuePrim.listWqueue().to[List])

  def getWqueue(visitId: Int): IO[Wqueue] =
    mysql(DbWqueuePrim.getWqueue(visitId).unique)

  def findWqueue(visitId: Int): IO[Option[Wqueue]] =
    mysql(DbWqueuePrim.getWqueue(visitId).option)

  def updateWqueue(wq: Wqueue): IO[AppEvent] =
    mysql(DbWqueuePrim.updateWqueue(wq))

  def changeWqueueState(visitId: Int, newState: WaitState): IO[(AppEvent, Wqueue)] =
    mysql(DbWqueuePrim.changeWqueueState(visitId, newState))

  def enterWqueue(wq: Wqueue): IO[AppEvent] =
    mysql(DbWqueuePrim.enterWqueue(wq))