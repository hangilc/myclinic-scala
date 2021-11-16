package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.myclinic.scala.webclient.ParamsImplicits.given
import scala.language.implicitConversions

object MasterApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def getIyakuhinMaster(iyakuhincode: Int, at: LocalDate): Future[IyakuhinMaster] =
      get("get-iyakuhin-master", Params("iyakuhincode" -> iyakuhincode, "at" -> at))

    def batchResolveIyakuhinMaster(iyakuhincodes: List[Int], at: LocalDate)
      : Future[Map[Int, IyakuhinMaster]] =
        post("batch-resolve-iyakuhin-master", Params("at" -> at), iyakuhincodes)
        
    def getShinryouMaster(shinryoucode: Int, at: LocalDate): Future[ShinryouMaster] =
      get("get-shinryou-master", Params("shinryoucode" -> shinryoucode, "at" -> at))

    def batchResolveShinryouMaster(shinryoucodes: List[Int], at: LocalDate)
      : Future[Map[Int, ShinryouMaster]] =
        post("batch-resolve-shinryou-master", Params("at" -> at), shinryoucodes)

    def getKizaiMaster(kizaicode: Int, at: LocalDate): Future[KizaiMaster] =
      get("get-kizai-master", Params("kizaicode" -> kizaicode, "at" -> at))

    def batchResolveKizaiMaster(kizaicodes: List[Int], at: LocalDate)
      : Future[Map[Int, KizaiMaster]] =
        post("batch-resolve-kizai-master", Params("at" -> at), kizaicodes)
