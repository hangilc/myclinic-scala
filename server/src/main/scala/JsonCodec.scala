package dev.myclinic.scala.server

import dev.myclinic.scala.model._
import dev.myclinic.scala.server.Main.ApiService

case class JsonCodecFromApi(api: ApiService) extends ModelJsonCodec {
  import api._
  def encodeAppoint(value: Appoint): String = api.toJson(value)
  def decodeAppoint(src: String): Appoint = api.fromJson[Appoint](src)

}

