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

object DbShahokokuhoPrim:
  def getShahokokuho(shahokokuhoId: Int): Query0[Shahokokuho] =
    sql"""
      select * from hoken_shahokokuho where shahokokuho_id = $shahokokuhoId
    """.query[Shahokokuho]

  def getAvailableShahokokuho(patientId: Int, at: LocalDate): Query0[Shahokokuho] =
    sql"""
      select * from hoken_shahokokuho where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
    """.query[Shahokokuho]

  def listShahokokuho(patientId: Int): ConnectionIO[List[Shahokokuho]] =
    sql"""
      select * from hoken_shahokokuho where patient_id = ${patientId}
      order by shahokokuho_id desc
    """.query[Shahokokuho].to[List]