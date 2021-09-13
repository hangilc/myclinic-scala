package dev.myclinic.scala.webclient

import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.raw.Event
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode

object Ajax {

  def request[T](
      method: String,
      url: String,
      params: Params,
      body: String
  )(using Decoder[T]): Future[T] = {
    val urlWithQuery = if( params.isEmpty ){
      url
    } else {
      url + "?" + params.encode()
    }
    val promise = Promise[T]
    val xhr = new XMLHttpRequest()
    xhr.onreadystatechange = (_: Event) => {
      if (xhr.readyState == XMLHttpRequest.DONE) {
        val status = xhr.status
        if (status == 200) {
          val src = xhr.responseText
          decode[T](src) match {
            case Right(value) => promise.success(value)
            case Left(ex)     => promise.failure(ex)
          }
        } else {
          System.err.println(xhr.responseText)
          promise.failure(new RuntimeException(xhr.responseText))
        }
      }
    }
    xhr.open(method, urlWithQuery)
    xhr.send()
    promise.future
  }

}
