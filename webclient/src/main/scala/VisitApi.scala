package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions

object VisitApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def getVisit(visitId: Int): Future[Visit] =
      get("get-visit", Params("visit-id" -> visitId))

    def batchGetVisit(visitIds: List[Int]): Future[Map[Int, Visit]] =
      post("batch-get-visit", Params(), visitIds)

    def deleteVisit(visitId: Int): Future[Boolean] =
      get("delete-visit", Params("visit-id" -> visitId))

    def getVisitEx(visitId: Int): Future[VisitEx] =
      get("get-visit-ex", Params("visit-id" -> visitId))