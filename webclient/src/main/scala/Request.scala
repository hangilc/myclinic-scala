package dev.myclinic.scala.webclient

import scala.concurrent.Future
import org.scalajs.dom.raw.{XMLHttpRequest, ProgressEvent}
import scala.concurrent.Promise
import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer

abstract class Request[T]:
  val xhr = new XMLHttpRequest()
  def responseType: String = "text"
  def onload(event: ProgressEvent): T

  def send(method: String, url: String, data: js.Any = null): Future[T] =
    val promise = Promise[T]
    xhr.responseType = responseType
    xhr.onload = (event: ProgressEvent) => {
      val t = onload(event)
      promise.success(t)
    }
    xhr.onerror = (event: ProgressEvent) => {
      promise.failure(new RuntimeException(xhr.responseText))
    }
    xhr.open(method, url, true)
    xhr.send(data)
    promise.future

class BinaryRequest extends Request[ArrayBuffer]:
  override def responseType = "arraybuffer"
  def onload(event: ProgressEvent): ArrayBuffer =
    xhr.response.asInstanceOf[ArrayBuffer]
