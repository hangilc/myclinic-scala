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

object DbRoujinPrim:
  def getRoujin(roujinId: Int): Query0[Roujin] =
    sql"""
      select * from hoken_roujin where roujin_id = $roujinId
    """.query[Roujin]


  def getAvailableRoujin(patientId: Int, at: LocalDate): Query0[Roujin] =
    sql"""
      select * from hoken_roujin where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
    """.query[Roujin]