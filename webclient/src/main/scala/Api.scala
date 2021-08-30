package dev.myclinic.scala.webclient

import endpoints4s.xhr
import dev.myclinic.scala.api.ApiEndpoints
import scala.concurrent.ExecutionContext.Implicits.global

object implicits {
  implicit val executionContext = global
}

object Api extends ApiEndpoints
  with xhr.future.Endpoints
  with xhr.JsonEntitiesFromSchemas {

}