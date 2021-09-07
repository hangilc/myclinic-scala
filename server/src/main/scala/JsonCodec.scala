package dev.myclinic.scala.server

import dev.myclinic.scala.model._
import dev.myclinic.scala.server.Main.ApiService

case class JsonCodecFromApi(api: ApiService) extends ModelJsonCodec {
  import api._
  def encode(value: Appoint): String = api.toJson[Appoint](value)
  def decode(json: String): Appoint = api.fromJson[Appoint](json)
  def encodeAppoint(value: Appoint): String = api.toJson(value)
  def decodeAppoint(src: String): Appoint = api.fromJson[Appoint](src)

}
