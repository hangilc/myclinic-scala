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
  
  def findShuushokugoMasterByName(name: String): IO[Option[ShuushokugoMaster]] =
    mysql(Prim.getShuushokugoMasterByName(name).option)

  def searchShuushokugoMaster(text: String): IO[List[ShuushokugoMaster]] =
    mysql(Prim.searchShuushokugoMaster(text).to[List])
