package dev.myclinic.scala.server

import dev.myclinic.scala.model._
import dev.myclinic.scala.server.Main.ApiService
import dev.myclinic.scala.api.ApiEndpoints
import endpoints4s.http4s.server
import endpoints4s.Valid
import endpoints4s.Invalid
import cats.effect.IO

// case class JsonCodecFromApi(api: ApiService) extends ModelJsonCodec {
//   import api._
//   def encode(value: Appoint): String = api.toJson[Appoint](value)
//   def decode(json: String): Appoint = api.fromJson[Appoint](json)
//   def encodeAppoint(value: Appoint): String = api.toJson(value)
//   def decodeAppoint(src: String): Appoint = api.fromJson[Appoint](src)

// }

object ServerJsonCodec
    extends server.Endpoints[IO]
    with ApiEndpoints
    with server.JsonEntitiesFromSchemas {

  def toJson[T](value: T)(implicit schema: JsonSchema[T]): String = {
    schema.stringCodec.encode(value).toString()
  }

  def fromJson[T](src: String)(implicit schema: JsonSchema[T]): T = {
    schema.stringCodec.decode(src) match {
      case Valid(t)   => t
      case Invalid(e) => throw new RuntimeException(e.toString())
    }
  }
}

object JsonableImplicits {
  import ServerJsonCodec._

  implicit class ToJsonable[T](value: T)(implicit schema: JsonSchema[T]) {
    def asJson = toJson[T](value)
  }

  implicit class FromJsonable[T](json: String) {
    def as[T](implicit schema: JsonSchema[T]): T = fromJson[T](json)
  }

}
