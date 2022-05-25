package dev.myclinic.scala.server

import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model.*
import java.time.LocalDate
import cats.effect.IO

object Helper:
  def findShinryouMasterByName(name: String, at: LocalDate): IO[Option[ShinryouMaster]] =
    val mapCode: Int = ConfigService.masterNameMap.shinryou.applyOrElse(name, _ => 0)
    if mapCode > 0 then
      Db.findShinryouMaster(mapCode, at)
    else
      Db.findShinryouMasterByName(name, at)

  def findShinryoucodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    ???

    