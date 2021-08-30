package dev.fujiwara.domq

import scala.scalajs.js

object DomqUtil {
  def alert(msg: String): Unit = js.Dynamic.global.alert(msg)
}