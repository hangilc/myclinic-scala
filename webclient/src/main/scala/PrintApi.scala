package dev.myclinic.scala.webclient

import scala.concurrent.Future
import dev.myclinic.scala.webclient.ParamsImplicits.given
import scala.language.implicitConversions
import dev.fujiwara.scala.drawer.PrintRequest

object PrintApi extends ApiBase:
  def baseUrl: String = "http://localhost:48080/"

  trait Api:
    def beep(): Future[Unit] =
      get("beep", Params())

    def listPrintSetting(): Future[List[String]] =
      get("setting/", Params())

    def getPrintPref(kind: String): Future[Option[String]] =
      get("pref/" + kind, Params())

    def printDrawer(req: PrintRequest, setting: Option[String]): Future[Boolean] =
      val sub: String = setting.getOrElse("")
      post("print/" + sub, Params(), req)
