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

  def listKouhi(patientId: Int): IO[List[Kouhi]] =
    mysql(DbKouhiPrim.listKouhi(patientId))

  def enterKouhi(kouhi: Kouhi): IO[(Kouhi, AppEvent)] =
    mysql(DbKouhiPrim.enterKouhi(kouhi))

  def getKouhi(kouhiId: Int): IO[Kouhi] =
    mysql(DbKouhiPrim.getKouhi(kouhiId).unique)

  def countKouhiUsage(kouhiId: Int): IO[Int] =
    mysql(DbKouhiPrim.countKouhiUsage(kouhiId))

  def countKouhiUsageBefore(kouhiId: Int, date: LocalDate): IO[Int] =
    mysql(DbKouhiPrim.countKouhiUsageBefore(kouhiId, date))

  def countKouhiUsageAfter(kouhiId: Int, date: LocalDate): IO[Int] =
    mysql(DbKouhiPrim.countKouhiUsageAfter(kouhiId, date))

