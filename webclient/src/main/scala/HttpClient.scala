package dev.myclinic.scala.webclient

import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.raw.Event
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode

// object HttpClient {
//   def get[R](url: String, params: Params)(using Decoder[R]): Future[R] =
//     Ajax.request("GET", url, params, "")

//   def post[R](
//       url: String,
//       params: Params,
//       jsonBody: String
//   )(using Decoder[R]): Future[R] =
//     Ajax.request("POST", url, params, jsonBody)
// }

object Ajax {

  def request[T](
      method: String,
      url: String,
      params: Params,
      body: String
  )(using Decoder[T]): Future[T] = {
    val promise = Promise[T]
    val xhr = new XMLHttpRequest()
    xhr.onreadystatechange = (_: Event) => {
      if (xhr.readyState == XMLHttpRequest.DONE) {
        val status = xhr.status
        if (status == XMLHttpRequest.DONE) {
          val src = xhr.responseText
          decode[T](src) match {
            case Right(value) => promise.success(value)
            case Left(ex)     => promise.failure(ex)
          }
        } else {
          promise.failure(new RuntimeException(xhr.responseText))
        }
      }
    }
    xhr.open(method, url)
    xhr.send()
    promise.future
  }

}
