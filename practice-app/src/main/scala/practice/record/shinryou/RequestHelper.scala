package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import java.time.LocalDate
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.*
import cats.data.EitherT
import cats.*
import cats.syntax.all.*

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

  def composeShinryouReqs(
      names: List[String],
      at: LocalDate,
      visitId: Int
  ): Future[Either[String, List[Shinryou]]] =
    names.map(shinryou(_, at, visitId)).sequence.map(_.sequence)

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

  def batchEnter(
      shinryouList: List[Shinryou] = List.empty,
      conductList: List[CreateConductRequest] = List.empty
  ): Future[(List[Int], List[Int])] =
    val req = CreateShinryouConductRequest(shinryouList, conductList)
    for
      result <- Api.batchEnterShinryouConduct(req)
      (shinryouIds, conductIds) = result
    yield (shinryouIds, conductIds)

  def enterShinryou(shinryou: Shinryou): Future[Int] =
    for
      result <- batchEnter(List(shinryou), List.empty)
      (shinryouIds, _) = result
    yield shinryouIds.head
