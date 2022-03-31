package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbShinryouMasterPrim => Prim}
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

trait DbShinryouMaster extends Mysql:
  def getShinryouMaster(shinryoucode: Int, at: LocalDate): IO[ShinryouMaster] =
    mysql(Prim.getShinryouMaster(shinryoucode, at).unique)

  def findShinryouMaster(shinryoucode: Int, at: LocalDate): IO[Option[ShinryouMaster]] =
    mysql(Prim.getShinryouMaster(shinryoucode, at).option)

  def batchResolveShinryouMaster(shinryoucodes: List[Int], at: LocalDate): IO[Map[Int, ShinryouMaster]] =
    val op = shinryoucodes.map(code => Prim.getShinryouMaster(code, at).unique)
      .sequence
      .map(items => Map(items.map(m => (m.shinryoucode, m)): _*))
    mysql(op)