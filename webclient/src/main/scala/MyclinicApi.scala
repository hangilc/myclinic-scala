package dev.myclinic.scala.webclient

import org.scalajs.dom._
import scala.concurrent.Future
import scala.concurrent.Promise
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.model._
import dev.myclinic.scala.modeljson.Implicits.{given}

object MyclinicApi {

  def hello(): Future[String] = {
    val promise = Promise[String]()
    val httpRequest = new XMLHttpRequest()
    httpRequest.onreadystatechange = (_: Event) => {
      if (httpRequest.readyState == XMLHttpRequest.DONE) {
        //val status = httpRequest.status
        promise.success(
          decode[String](httpRequest.responseText).getOrElse(
            throw new RuntimeException("JSON error")
          )
        )
      }
    }
    httpRequest.open("GET", "http://localhost:8080/api/hello")
    httpRequest.send()
    promise.future
  }

}
