package dev.myclinic.scala.webclient

import endpoints4s.xhr
import dev.myclinic.scala.api.ApiEndpoints

object Api extends ApiEndpoints
  with xhr.future.Endpoints
  with xhr.JsonEntitiesFromSchemas {

}