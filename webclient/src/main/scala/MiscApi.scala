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
