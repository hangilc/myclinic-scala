package dev.myclinic.scala.webclient

import org.scalajs.dom._
import scala.concurrent.Future
import scala.concurrent.Promise

object MyclinicApi {
  
  def hello(): Future[String] = {
    val promise = Promise[String]()
    val httpRequest = new XMLHttpRequest()
    httpRequest.onreadystatechange = (_:Event) => {
      if( httpRequest.readyState == XMLHttpRequest.DONE){
        //val status = httpRequest.status
        promise.success(httpRequest.responseText)
      }
    }
    httpRequest.open("GET", "http://localhost:8080/api/hello")
    httpRequest.send()
    promise.future
  }

}
