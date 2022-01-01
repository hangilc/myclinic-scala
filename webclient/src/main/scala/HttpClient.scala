package dev.myclinic.scala.webclient

import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.Event
import io.circe._
import io.circe.syntax._
import io.circe.parser.*
import org.scalajs.dom.ProgressEvent
import scala.scalajs.js.typedarray.ArrayBuffer

object Ajax:
  def request[T](
      method: String,
      url: String,
      params: Params,
      body: String | ArrayBuffer,
      progress: Option[(Double, Double) => Unit] = None,
      resultHandler: Option[XMLHttpRequest => T] = None
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
          resultHandler match {
            case Some(handler) => promise.success(handler(xhr))
            case None => {
              val src = xhr.responseText
              decode[T](src) match
                case Right(value) => promise.success(value)
                case Left(ex) => {
                  System.err.println(("decode-error", src, ex.getMessage));
                  promise.failure(ex)
                }
            }
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
    progress match {
      case Some(prog) =>
        xhr.onprogress = (evt: ProgressEvent) => prog(evt.loaded, evt.total)
      case None => ()
    }
    xhr.open(method, urlWithQuery)
    body match {
      case b: String => xhr.send(b)
      case b: ArrayBuffer => {
        xhr.setRequestHeader("content-type", "application/octet-sream")
        xhr.send(b)
      }
    }
    promise.future


