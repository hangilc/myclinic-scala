package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbConductShinryouPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbDisease extends Mysql:
  def listCurrentDisease(patientId: Int): IO[List[Disease]] =
    mysql(DbDiseasePrim.listCurrentDisease(patientId).to[List])
