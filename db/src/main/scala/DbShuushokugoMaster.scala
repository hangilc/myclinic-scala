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
import org.http4s.Header.ToRaw.Primitive

trait DbShuushokugoMaster extends Mysql:
  import DbShuushokugoMasterPrim as Prim

  def findShuushokugoMaster(
      shuushokugocode: Int,
      at: LocalDate
  ): IO[Option[ShuushokugoMaster]] =
    mysql(Prim.getShuushokugoMaster(shuushokugocode, at).option)

  def getShuushokugoMaster(
      shuushokugocode: Int,
      at: LocalDate
  ): IO[ShuushokugoMaster] =
    mysql(Prim.getShuushokugoMaster(shuushokugocode, at).unique)

  def findShuushokugoMasterByName(
      name: String,
      at: LocalDate
  ): IO[Option[ShuushokugoMaster]] =
    mysql(Prim.getShuushokugoMasterByName(name, at).option)

  def searchShuushokugoMaster(
      text: String,
      at: LocalDate
  ): IO[List[ShuushokugoMaster]] =
    mysql(Prim.searchShuushokugoMaster(text, at).to[List])
