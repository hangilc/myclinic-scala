package dev.myclinic.scala.webclient

import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.modeljson.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions
import scala.concurrent.Future

trait ApiBase(urlBase: String):
  def url(service: String): String = s"${urlBase}/${service}"

  def get[T](service: String, params: Params)(using
      Decoder[T]
  ): Future[T] =
    Ajax.request("GET", url(service), params, "")

  def post[B, T](service: String, params: Params, body: B)(using
      Encoder[B],
      Decoder[T]
  ): Future[T] =
    Ajax.request("POST", url(service), params, body.asJson.toString())

  def post[T](service: String, params: Params)(using Decoder[T]): Future[T] =
    post[Map[String, String], T](service, params, Map.empty)

