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

trait DbShahokokuho extends Mysql:
  def findAvailableShahokokuho(patientId: Int, at: LocalDate): IO[Option[Shahokokuho]] =
    mysql(DbShahokokuhoPrim.getAvailableShahokokuho(patientId, at).option)

  def listShahokokuho(patientId: Int): IO[List[Shahokokuho]] =
    mysql(DbShahokokuhoPrim.listShahokokuho(patientId))

  def enterShahokokuho(shahokokuho: Shahokokuho): IO[AppEvent] =
    mysql(DbShahokokuhoPrim.enterShahokokuho(shahokokuho))

  def updateShahokokuho(shahokokuho: Shahokokuho): IO[AppEvent] =
    mysql(DbShahokokuhoPrim.updateShahokokuho(shahokokuho))

    