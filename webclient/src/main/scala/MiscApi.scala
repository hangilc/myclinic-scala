package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import dev.myclinic.scala.clinicop.*
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions

object MiscApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def resolveClinicOperation(date: LocalDate): Future[ClinicOperation] =
      get("resolve-clinic-operation", Params("date" -> date))

    def batchResolveClinicOperations(
        dates: List[LocalDate]
    ): Future[Map[LocalDate, ClinicOperation]] =
      post("batch-resolve-clinic-operations", Params(), dates)

    def postHotline(hotline: Hotline): Future[Boolean] =
      post("post-hotline", Params(), hotline)

    def listTodaysHotline(): Future[List[HotlineCreated]] =
      get("list-todays-hotline", Params())

    def listWqueue(): Future[List[Wqueue]] =
      get("list-wqueue", Params())

    def listDrugForVisit(visitId: Int): Future[List[Drug]] =
      get("list-drug-for-visit", Params("visit-id" -> visitId))

    def listShinryouForVisit(visitId: Int): Future[List[Shinryou]] =
      get("list-shinryou-for-visit", Params("visit-id" -> visitId))

    def listConductForVisit(visitId: Int): Future[List[Conduct]] =
      get("list-conduct-for-visit", Params("visit-id" -> visitId))

    def listConductDrugForVisit(conductId: Int): Future[List[ConductDrug]] =
      get("list-conduct-drug-for-visit", Params("visit-id" -> conductId))

    def listConductShinryouForVisit(conductId: Int): Future[List[ConductShinryou]] =
      get("list-conduct-shinryou-for-visit", Params("visit-id" -> conductId))

    def listConductKizaiForVisit(conductId: Int): Future[List[ConductKizai]] =
      get("list-conduct-kizai-for-visit", Params("visit-id" -> conductId))

    def getMeisai(visitId: Int): Future[Meisai] =
      get("get-meisai", Params("visit-id" -> visitId))
