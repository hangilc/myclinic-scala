package dev.myclinic.scala.webclient

import scala.concurrent.Future

object ConfigApi extends ApiBase:

  def baseUrl: String = "/api/"

  trait Api:
    def getShinryouRegular(): Future[Map[String, List[String]]] =
      get("get-shinryou-regular", Params())    

    def getShinryouKensa(): Future[Map[String, List[String]]] =
      get("get-shinryou-kensa", Params())


      