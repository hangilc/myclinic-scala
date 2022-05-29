package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.*
import cats.data.EitherT

object RequestHelper:

  def shinryou(
      name: String,
      at: LocalDate,
      visitId: Int
  ): Future[Either[String, Shinryou]] =
    (for
      shinryoucode <- EitherT.fromOptionF(
        Api.resolveShinryoucodeByName(name, at),
        s"${name} のコードをみつけられませんでした。"
      )
    yield Shinryou(0, visitId, shinryoucode)).value

  def conductShinryouReq(
      name: String,
      at: LocalDate
  ): Future[Either[String, ConductShinryou]] =
    (for
      shinryoucode <- EitherT.fromOptionF(
        Api.resolveShinryoucodeByName(name, at),
        s"${name} のコードをみつけられませんでした。"
      )
    yield ConductShinryou(0, 0, shinryoucode)).value

  def conductKizaiReq(
      name: String,
      amount: Double,
      at: LocalDate
  ): Future[Either[String, ConductKizai]] =
    (for
      kizaicode <- EitherT.fromOptionF(
        Api.resolveKizaicodeByName(name, at),
        s"${name} のコードをみつけられませんでした。"
      )
    yield ConductKizai(0, 0, kizaicode, amount)).value
