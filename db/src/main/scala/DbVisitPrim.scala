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

object DbVisitPrim:
  def getVisit(visitId: Int): Query0[Visit] =
    sql"""
      select * from visit where visit_id = ${visitId}
    """.query[Visit]

  def deleteVisit(visitId: Int): ConnectionIO[Unit] =
    sql"""
      delete from visit where visit_id = ${visitId}
    """.update.run.map(affected => {
      if affected != 1 then
        throw new RuntimeException("Failed to delete visit.")
    })
