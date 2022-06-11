package dev.myclinic.scala.webclient

import org.scalajs.dom.{fetch, RequestInit}
import scala.concurrent.Future
import io.circe.*
import io.circe.parser.decode

trait FetchBase:
  def baseUrl: String

  def url(service: String): String = s"${baseUrl}${service}"

  def doFetch[T](service: String, params: Params, init: RequestInit)(
    using Decoder[T]
  ): Future[T] =
    val url = this.url(service)
    val urlWithQuery =
      if params.isEmpty then url
      else url + "?" + params.encode()
    val op =
      for
        response <- fetch(urlWithQuery, init).toFuture
        text <- response.text().toFuture
      yield decode[T](text)
    op.map {
      case Left(err) => throw new RuntimeException(err.toString)
      case Right(v) => v
    }
    
