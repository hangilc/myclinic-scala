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

object DbKoukikoureiPrim:
  def getKoukikourei(koukikoureiId: Int): Query0[Koukikourei] =
    sql"""
      select * from hoken_koukikourei where koukikourei_id = $koukikoureiId
    """.query[Koukikourei]


  def getAvailableKoukikourei(patientId: Int, at: LocalDate): Query0[Koukikourei] =
    sql"""
      select * from hoken_koukikourei where patient_id = ${patientId} 
        and valid_from <= ${at}
        and (valid_upto = '0000-00-00' || ${at} <= valid_upto)
    """.query[Koukikourei]

  def listKoukikourei(patientId: Int): ConnectionIO[List[Koukikourei]] =
    sql"""
      select * from hoken_koukikourei where patient_id = ${patientId}
      order by koukikourei_id desc
    """.query[Koukikourei].to[List]