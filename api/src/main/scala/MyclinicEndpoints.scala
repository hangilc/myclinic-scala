package dev.myclinic.scala.api

import sttp.tapir._
import sttp.tapir.json.circe._

object MyclinicEndpoints {

  val helloEndpoint: Endpoint[Unit, Unit, String, Any] = 
    endpoint.in("hello").out(plainBody[String])

}