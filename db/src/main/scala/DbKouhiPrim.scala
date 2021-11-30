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

object DbKouhiPrim:
  def getKouhi(kouhiId: Int): Query0[Kouhi] =
    sql"""
      select * from hoken_kouhi where kouhi_id = $kouhiId
    """.query[Kouhi]


  def listAvailableKouhi(patientId: Int, at: LocalDate): ConnectionIO[List[Kouhi]] =
    sql"""
      select * from kouhi where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
        order by kouhi_id
    """.query[Kouhi].to[List]