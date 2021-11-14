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
    val op = sql"""
      select visit_id, wait_state from wqueue order by visit_id
    """.query[(Int, Int)].map(tuple => tuple match {
      case (visitId, waitStateCode) => {
        val waitState = WaitState.fromCode(waitStateCode)
        Wqueue(visitId, waitState)
      }
    }).to[List]
    mysql(op)