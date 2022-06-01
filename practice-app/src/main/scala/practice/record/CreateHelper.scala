package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import cats.syntax.all.*
import java.time.LocalDate
import cats.data.EitherT

object CreateHelper:
  def batchEnter(
      shinryouList: List[Shinryou],
      conductList: List[CreateConductRequest]
  ): Future[(List[ShinryouEx], List[ConductEx])] =
    for
      result <- Api.batchEnterShinryouConduct(
        CreateShinryouConductRequest(
          shinryouList,
          conductList
        )
      )
      (shinryouIds, conductIds) = result
      shinryouExList <- shinryouIds.map(id => Api.getShinryouEx(id)).sequence
      conductExList <- conductIds.map(id => Api.getConductEx(id)).sequence
    yield (shinryouExList, conductExList)

  def batchEnterShinryou(
      shinryouList: List[Shinryou]
  ): Future[List[ShinryouEx]] =
    batchEnter(shinryouList, List.empty).map(_.head)

  def batchEnterConduct(
      conductList: List[CreateConductRequest]
  ): Future[List[ConductEx]] =
    batchEnter(List.empty, conductList).map(_(1))

  def batchEnterShinryouByName(
      names: List[String],
      at: LocalDate,
      visitId: Int
  ): EitherT[Future, String, List[ShinryouEx]] =
    for
      shinryoucodes <- names
        .map(name => CodeResolver.resolveShinryoucodeByName(name, at))
        .sequence
      shinryouList = shinryoucodes.map(code => Shinryou(0, visitId, code))
      shinryouExList <- EitherT.right(batchEnterShinryou(shinryouList))
    yield shinryouExList

  def enterShinryou(shinryou: Shinryou): Future[ShinryouEx] =
    batchEnterShinryou(List(shinryou)).map(_.head)

  def enterConduct(conduct: CreateConductRequest): Future[ConductEx] =
    batchEnterConduct(List(conduct)).map(_.head)

  def shinryouReqByName(
      name: String,
      at: LocalDate,
      visitId: Int
  ): EitherT[Future, String, Shinryou] =
    for shinryoucode <- CodeResolver.resolveShinryoucodeByName(name, at)
    yield Shinryou(0, visitId, shinryoucode)

  def conductShinryouReqByName(
      name: String,
      at: LocalDate
  ): EitherT[Future, String, ConductShinryou] =
    for shinryoucode <- CodeResolver.resolveShinryoucodeByName(name, at)
    yield ConductShinryou(0, 0, shinryoucode)

  def conductKizaiReqByName(
      name: String,
      amount: Double,
      at: LocalDate
  ): EitherT[Future, String, ConductKizai] =
    for kizaicode <- CodeResolver.resolveKizaicodeByName(name, at)
    yield ConductKizai(0, 0, kizaicode, amount)
