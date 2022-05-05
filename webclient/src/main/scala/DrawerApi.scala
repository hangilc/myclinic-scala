package dev.myclinic.scala.webclient

import io.circe._
import io.circe.syntax._
import dev.fujiwara.scala.drawer.Op
import scala.concurrent.Future
import dev.myclinic.scala.webclient.ParamsImplicits.given

object DrawerApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def shohousenDrawer(textId: Int): Future[List[Op]] =
      get("shohousen-drawer", Params("text-id" -> textId))