package dev.myclinic.scala.api

import endpoints4s.algebra
import endpoints4s.generic
import dev.myclinic.scala.model._
import java.time.LocalDate
import java.time.LocalTime

case class Result(value: Int)

trait AppointEndpoints 
  extends algebra.Endpoints 
  with algebra.JsonEntitiesFromSchemas
  with generic.JsonSchemas {

  val root = path / "api"

  val currentValue: Endpoint[Unit, Result] = {
    endpoint(get(root / "current-value"), ok(jsonResponse[Result]))
  }

  val listAppoint: Endpoint[(LocalDate, LocalTime), List[Appoint]] = {
    endpoint(get(root / "list-appoint"), ok(jsonResponse[List[Appoint]]))
  }

  implicit lazy val resultSchema: JsonSchema[Result] = genericJsonSchema
}
