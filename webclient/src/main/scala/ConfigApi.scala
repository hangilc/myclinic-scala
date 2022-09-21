package dev.myclinic.scala.webclient

import scala.concurrent.Future
import dev.myclinic.scala.model.*

object ConfigApi extends ApiBase:

  def baseUrl: String = "/api/"

  trait Api:
    def getShinryouRegular(): Future[Map[String, List[String]]] =
      get("get-shinryou-regular", Params())    

    def getShinryouKensa(): Future[Map[String, List[String]]] =
      get("get-shinryou-kensa", Params())

    def listDiseaseExample(): Future[List[DiseaseExample]] =
      get("list-disease-example", Params())

    def defaultKoukikoureiHokenshaBangou(): Future[Int] =
      get("default-koukikourei-hokensha-bangou", Params())

    def getPhonebook(): Future[String] =
      get("get-phonebook", Params());


      