package dev.myclinic.scala.server

import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model.*
import java.time.LocalDate
import cats.effect.IO
import cats.data.OptionT

object Helper:
  def resolveShinryoucodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    ConfigService.masterNameMap.shinryou
      .get(name)
      .fold(
        Db.findShinryoucodeByName(name, at)
      )(nameCode =>
        OptionT(resolveShinryoucode(nameCode, at))
          .orElse(OptionT(Db.findShinryoucodeByName(name, at)))
          .value
      )

  def resolveShinryoucode(shinryoucode: Int, at: LocalDate): IO[Option[Int]] =
    val code: Int =
      ConfigService.masterTransition.shinryou.transit(shinryoucode, at)
    val opt =
      for master <- OptionT(Db.findShinryouMaster(code, at))
      yield master.shinryoucode
    opt.value

  def resolveKizaicodeByName(name: String, at: LocalDate): IO[Option[Int]] =
    ConfigService.masterNameMap.kizai
      .get(name)
      .fold(
        Db.findKizaicodeByName(name, at)
      )(nameCode =>
        OptionT(resolveKizaicode(nameCode, at))
          .orElse(OptionT(Db.findKizaicodeByName(name, at)))
          .value
      )

  def resolveKizaicode(kizaicode: Int, at: LocalDate): IO[Option[Int]] =
    val code: Int = ConfigService.masterTransition.kizai.transit(kizaicode, at)
    val opt =
      for master <- OptionT(Db.findKizaiMaster(code, at))
      yield master.kizaicode
    opt.value

  def resolveByoumeiMasterByName(
      name: String,
      at: LocalDate
  ): IO[Option[ByoumeiMaster]] =
    val op = for
      nameCode <- OptionT.fromOption[IO](
        ConfigService.masterNameMap.byoumei.get(name)
      )
      transCode = ConfigService.masterTransition.byoumei.transit(nameCode, at)
      master <- OptionT(Db.findByoumeiMaster(transCode, at))
    yield master
    op.orElse(OptionT(Db.findByoumeiMasterByName(name, at))).value

  def resolveByoumeicode(byoumeicode: Int, at: LocalDate): IO[Option[Int]] =
    val code: Int =
      ConfigService.masterTransition.byoumei.transit(byoumeicode, at)
    val opt =
      for master <- OptionT(Db.findByoumeiMaster(code, at))
      yield master.shoubyoumeicode
    opt.value

  def resolveShuushokugoMasterByName(
      name: String,
      at: LocalDate
  ): IO[Option[ShuushokugoMaster]] =
    val op = for
      nameCode <- OptionT.fromOption[IO](
        ConfigService.masterNameMap.shuushokugo.get(name)
      )
      transCode = ConfigService.masterTransition.shuushokugo.transit(nameCode, at)
      master <- OptionT(Db.findShuushokugoMaster(transCode, at))
    yield master
    op.orElse(OptionT(Db.findShuushokugoMasterByName(name, at))).value

  def resolveShuushokugocode(
      shuushokugocode: Int,
      at: LocalDate
  ): IO[Option[Int]] =
    val code: Int =
      ConfigService.masterTransition.shuushokugo.transit(shuushokugocode, at)
    val opt =
      for master <- OptionT(Db.findShuushokugoMaster(code, at))
      yield master.shuushokugocode
    opt.value
