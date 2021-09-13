package dev.myclinic.scala.webclient

import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.raw.Event
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.modeljson.Implicits.{given}

class HttpClient(prefix: String) {
  def get[R](url: String): Future[R] = ???
  def post[R](url: String, jsonBody: String): Future[R] = ???
}

object Ajax {

  def request[T](
      method: String,
      url: String,
      params: Seq[(String, ParamValue)],
      body: String
  ): Future[T] = {
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
