package dev.myclinic.scala.server

import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model.*
import java.time.LocalDate
import cats.effect.IO
import cats.data.OptionT

object Helper:
  // def findShinryouMasterByName(name: String, at: LocalDate): IO[Option[ShinryouMaster]] =
  //   val mapCode: Int = ConfigService.masterNameMap.shinryou.applyOrElse(name, _ => 0)
  //   if mapCode > 0 then
  //     Db.findShinryouMaster(mapCode, at)
  //   else
  //     Db.findShinryouMasterByName(name, at)

  def findShinryoucodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    val opt: OptionT[IO, Int] = 
      OptionT.fromOption[IO](ConfigService.masterNameMap.shinryou.get(name))
        .map(code => ConfigService.masterTransition.shinryou.transit(code, at))
        .flatMap(code => OptionT(Db.findShinryouMaster(code, at)))
        .map(_.shinryoucode)
        .orElse(OptionT(Db.findShinryoucodeByName(name, at)))
    opt.value

  def findKizaicodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    val opt: OptionT[IO, Int] = 
      OptionT.fromOption[IO](ConfigService.masterNameMap.kizai.get(name))
        .map(code => ConfigService.masterTransition.kizai.transit(code, at))
        .flatMap(code => OptionT(Db.findKizaiMaster(code, at)))
        .map(_.kizaicode)
        .orElse(OptionT(Db.findKizaicodeByName(name, at)))
    opt.value


