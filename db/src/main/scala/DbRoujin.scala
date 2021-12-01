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

trait DbRoujin extends Mysql:
  def findAvailableRoujin(patientId: Int, at: LocalDate): IO[Option[Roujin]] =
    mysql(DbRoujinPrim.getAvailableRoujin(patientId, at).option)

  def listRoujin(patientId: Int): IO[List[Roujin]] =
    mysql(DbRoujinPrim.listRoujin(patientId))