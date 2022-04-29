package dev.myclinic.scala.webclient

import dev.fujiwara.scala.drawer.Op
import dev.fujiwara.scala.drawer.Op.given
import scala.concurrent.Future
import dev.myclinic.scala.webclient.ParamsImplicits.given

object DrawerApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def shohousenDrawer(textId: Int): Future[List[Op]] =
      get("shohousen-drawer", Params("text-id" -> textId))