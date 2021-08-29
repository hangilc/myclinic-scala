package dev.myclinic.scala.api

import endpoints4s.algebra
import endpoints4s.generic
import dev.myclinic.scala.model._
import java.time.LocalDate
import java.time.LocalTime
import scala.util.Try
import scala.util.Success
import endpoints4s.Valid
import scala.util.Failure
import endpoints4s.Invalid
import java.time.format.DateTimeFormatter
import endpoints4s.Validated

trait ApiEndpoints
    extends algebra.Endpoints
    with algebra.JsonEntitiesFromSchemas
    with generic.JsonSchemas {

  val root = path / "api"

  val listAppoint: Endpoint[(LocalDate, LocalDate), List[Appoint]] = {
    endpoint(
      get(
        root / "list-appoint" /? (qs[LocalDate]("from") & qs[LocalDate]("upto"))
      ),
      ok(jsonResponse[List[Appoint]])
    )
  }

  val registerAppoint: Endpoint[(LocalDate, LocalTime, String), Unit] = {
    endpoint(
      post(
        root / "register-appoint" /? (qs[LocalDate]("date") & qs[LocalTime]("time") & qs[String]("name")),
        emptyRequest
      ),
      ok(emptyResponse)
    )
  }

  implicit def dateScheme(implicit
      string: JsonSchema[String]
  ): JsonSchema[LocalDate] =
    string.xmapPartial(toDate)(fromDate)

  implicit def timeSchema(implicit
      string: JsonSchema[String]
  ): JsonSchema[LocalTime] =
    string.xmapPartial(toTime)(fromTime)

  implicit lazy val appointSchema: JsonSchema[Appoint] = genericJsonSchema

  def toDate: String => Validated[LocalDate] = { s =>
    Try(LocalDate.parse(s)) match {
      case Success(date) => Valid(date)
      case Failure(_)    => Invalid(s"Invalid date value '${s}'")
    }
  }

  def fromDate: LocalDate => String = _.toString

  implicit def dateParam(implicit
      string: QueryStringParam[String]
  ): QueryStringParam[LocalDate] =
    string.xmapPartial(toDate)(fromDate)

  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  def toTime: String => Validated[LocalTime] = { s =>
    Try(LocalTime.parse(s, timeFormatter)) match {
      case Success(time) => Valid(time)
      case Failure(_)    => Invalid(s"Invalid time value '${s}'")
    }
  }

  def fromTime: LocalTime => String = _.format(timeFormatter)

  implicit def timeParam(implicit
      string: QueryStringParam[String]
  ): QueryStringParam[LocalTime] =
    string.xmapPartial(toTime)(fromTime)

}
