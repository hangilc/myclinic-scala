package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.myclinic.scala.db.{DbAppointPrim => Prim}
import dev.myclinic.scala.db.DoobieMapping.*
import doobie.*
import doobie.implicits.*
import scala.math.Ordered.orderingToOrdered
import io.circe.parser.decode
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

trait DbByoumeiMaster extends Mysql:
  import DbByoumeiMasterPrim as Prim

  def findByoumeiMaster(shoubyoumeicode: Int, at: LocalDate): IO[Option[ByoumeiMaster]] =
    mysql(Prim.getByoumeiMaster(shoubyoumeicode, at).option)

  def getByoumeiMaster(shoubyoumeicode: Int, at: LocalDate): IO[ByoumeiMaster] =
    mysql(Prim.getByoumeiMaster(shoubyoumeicode, at).unique)

  def findByoumeiMasterByName(name: String, at: LocalDate): IO[Option[ByoumeiMaster]] =
    mysql(Prim.getByoumeiMasterByName(name, at).option)

  def searchByoumeiMaster(text: String, at: LocalDate): IO[List[ByoumeiMaster]] =
    mysql(Prim.searchByoumeiMaster(text, at).to[List])
