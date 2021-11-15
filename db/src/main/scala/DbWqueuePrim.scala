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

  def deleteWqueue(visitId: Int): ConnectionIO[Unit] =
    sql"""
      delete from wqueue where visit_id = ${visitId}
    """.update.run.map(affected => {
      if affected != 1 then
        throw new RuntimeException("Failed to delete wqueue.")
    })
    
  def tryDeleteWqueue(visitId: Int): ConnectionIO[Boolean] =
    sql"""
      delete from wqueue where visit_id = ${visitId}
    """.update.run.map({
      case 0 => false
      case 1 => true
      case _ => throw new RuntimeException("Failed to delete wqueue.")
    })
