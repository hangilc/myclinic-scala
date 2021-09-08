package dev.myclinic.scala.server

import dev.myclinic.scala.model._
import dev.myclinic.scala.api.ApiEndpoints
import endpoints4s.http4s.server
import endpoints4s.Valid
import endpoints4s.Invalid
import cats.effect.IO

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

object DbJsonEncoder extends dev.myclinic.scala.db.JsonEncoder {
  override def toJson(value: Appoint): String = ServerJsonCodec.toJson(value)
}
