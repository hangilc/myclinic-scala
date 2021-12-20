package dev.myclinic.scala.webclient

import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions
import scala.concurrent.Future
import org.scalajs.dom.raw.XMLHttpRequest
import scala.scalajs.js.typedarray.ArrayBuffer

trait ApiBase:
  def baseUrl: String

  def url(service: String): String = s"${baseUrl}${service}"

  def get[T](
      service: String,
      params: Params,
      progress: Option[(Double, Double) => Unit] = None,
      resultHandler: Option[XMLHttpRequest => T] = None
  )(using
      Decoder[T]
  ): Future[T] =
    Ajax.request("GET", url(service), params, "", progress, resultHandler)

  def getBinary(
    service: String,
    params: Params
  ): Future[ArrayBuffer] =
    val req = new BinaryDownloadRequest
    val reqUrl = url(service) + (
      if params.isEmpty then ""
      else "?" + params.encode()
    )
    req.send("GET", reqUrl)

  def post[B, T](service: String, params: Params, body: B)(using
      Encoder[B],
      Decoder[T]
  ): Future[T] =
    Ajax.request("POST", url(service), params, body.asJson.toString())

  def postBinary[T](service: String, params: Params, body: ArrayBuffer)(using
      Decoder[T]
  ): Future[T] =
    Ajax.request("POST", url(service), params, body)

  def post[T](service: String, params: Params)(using Decoder[T]): Future[T] =
    Ajax.request("POST", url(service), params, "{}")

  def put[B, T](service: String, params: Params, body: B)(using
      Encoder[B],
      Decoder[T]
  ): Future[T] =
    Ajax.request("PUT", url(service), params, body.asJson.toString())

  def put[T](service: String, params: Params)(using Decoder[T]): Future[T] =
    Ajax.request("PUT", url(service), params, "{}")

  def delete[B, T](service: String, params: Params, body: B)(using
      Encoder[B],
      Decoder[T]
  ): Future[T] =
    Ajax.request("DELETE", url(service), params, body.asJson.toString())

  def delete[T](service: String, params: Params)(using Decoder[T]): Future[T] =
    Ajax.request("DELETE", url(service), params, "{}")
