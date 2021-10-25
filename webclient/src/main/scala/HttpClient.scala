package dev.myclinic.scala.webclient

import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.raw.Event
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode

object Ajax:
  def request[T](
      method: String,
      url: String,
      params: Params,
      body: String
  )(using Decoder[T]): Future[T] =
    val urlWithQuery =
      if params.isEmpty then url
      else url + "?" + params.encode()
    val promise = Promise[T]
    val xhr = new XMLHttpRequest()
    xhr.onreadystatechange = (_: Event) => {
      if xhr.readyState == XMLHttpRequest.DONE then
        val status = xhr.status
        if status == 200 then
          val src = xhr.responseText
          decode[T](src) match
            case Right(value) => promise.success(value)
            case Left(ex) => {
              System.err.println(("decode-error", src, ex.getMessage)); promise.failure(ex)
            }
        else if status == 400 then
          if xhr.getResponseHeader("X-User-Error") == "true" then
            val message: String = decode[String](xhr.responseText) match
              case Right(m) => m
              case Left(e)  => xhr.responseText
            promise.failure(UserError(message))
          else promise.failure(new RuntimeException(xhr.response.toString()))
        else
          System.err.println(xhr.responseText)
          promise.failure(new RuntimeException(xhr.responseText))
    }
    xhr.open(method, urlWithQuery)
    xhr.send(body)
    promise.future
