package dev.myclinic.scala.server

import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model.*
import java.time.LocalDate
import cats.effect.IO
import cats.data.OptionT

object Helper:
  def resolveShinryoucodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    ConfigService.masterNameMap.shinryou.get(name).fold(
      Db.findShinryoucodeByName(name, at)
    )(
      nameCode =>
        OptionT(resolveShinryoucode(nameCode, at))
          .orElse(OptionT(Db.findShinryoucodeByName(name, at)))
          .value
    )

  def resolveShinryoucode(shinryoucode: Int, at: LocalDate): IO[Option[Int]] =
    val code: Int = ConfigService.masterTransition.shinryou.transit(shinryoucode, at)
    val opt = 
      for
        master <- OptionT(Db.findShinryouMaster(code, at))
      yield master.shinryoucode
    opt.value

  def resolveKizaicodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    ConfigService.masterNameMap.kizai.get(name).fold(
      Db.findKizaicodeByName(name, at)
    )(
      nameCode =>
        OptionT(resolveKizaicode(nameCode, at))
          .orElse(OptionT(Db.findKizaicodeByName(name, at)))
          .value
    )

  def resolveKizaicode(kizaicode: Int, at: LocalDate): IO[Option[Int]] =
    val code: Int = ConfigService.masterTransition.kizai.transit(kizaicode, at)
    val opt = 
      for
        master <- OptionT(Db.findKizaiMaster(code, at))
      yield master.kizaicode
    opt.value


