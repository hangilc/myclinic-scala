package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import cats.syntax.all.*
import java.time.LocalDate
import cats.data.EitherT

object CreateHelper:
  def batchEnterShinryou(shinryouList: List[Shinryou]): Future[List[ShinryouEx]] =
    val req = CreateShinryouConductRequest(
      shinryouList = shinryouList
    )
    for
      result <- Api.batchEnterShinryouConduct(req)
      (shinryouIds, _) = result
      entered <- shinryouIds.map(id => Api.getShinryouEx(id)).sequence
    yield entered

  def batchEnterConduct(conductList: List[CreateConductRequest]): Future[List[ConductEx]] =
    val req = CreateShinryouConductRequest(
      conducts = conductList
    )
    for
      result <- Api.batchEnterShinryouConduct(req)
      (_, conductIds) = result
      entered <- conductIds.map(id => Api.getConductEx(id)).sequence
    yield entered

  def enterConduct(conduct: CreateConductRequest): Future[ConductEx] =
    batchEnterConduct(List(conduct)).map(_.head)

  def conductShinryouReqByName(name: String, at: LocalDate): EitherT[Future, String, ConductShinryou] =
    for
      shinryoucode <- CodeResolver.resolveShinryoucodeByName(name, at)
    yield ConductShinryou(0, 0, shinryoucode)

  def conductKizaiReqByName(name: String, amount: Double, at: LocalDate): EitherT[Future, String, ConductKizai] =
    for
      kizaicode <- CodeResolver.resolveKizaicodeByName(name, at)
    yield ConductKizai(0, 0, kizaicode, amount)
    
