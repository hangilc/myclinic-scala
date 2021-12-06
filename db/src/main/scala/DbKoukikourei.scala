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

trait DbKoukikourei extends Mysql:
  def findAvailableKoukikourei(patientId: Int, at: LocalDate): IO[Option[Koukikourei]] =
    mysql(DbKoukikoureiPrim.listAvailableKoukikourei(patientId, at).map(_.headOption))

  def listAvailableKoukikourei(patientId: Int, at: LocalDate): IO[List[Koukikourei]] =
    mysql(DbKoukikoureiPrim.listAvailableKoukikourei(patientId, at))

  def listKoukikourei(patientId: Int): IO[List[Koukikourei]] =
    mysql(DbKoukikoureiPrim.listKoukikourei(patientId))