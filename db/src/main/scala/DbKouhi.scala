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

trait DbKouhi extends Mysql:
  def listAvailableKouhi(patientId: Int, at: LocalDate): IO[List[Kouhi]] =
    mysql(DbKouhiPrim.listAvailableKouhi(patientId, at))