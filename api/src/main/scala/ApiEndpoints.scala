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
import java.time.LocalDateTime

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
        root / "register-appoint" /? (qs[LocalDate]("date") & qs[LocalTime](
          "time"
        ) & qs[String]("name")),
        emptyRequest
      ),
      ok(emptyResponse)
    )
  }

  val cancelAppoint: Endpoint[(LocalDate, LocalTime, String), Unit] = {
    endpoint(
      post(
        root / "cancel-appoint" /? (qs[LocalDate]("date") & qs[LocalTime](
          "time"
        ) & qs[String]("name")),
        emptyRequest
      ),
      ok(emptyResponse)
    )
  }

  val getAppoint: Endpoint[(LocalDate, LocalTime), Appoint] = {
    endpoint(
      get(
        root / "get-appoint" /? (qs[LocalDate]("date") & qs[LocalTime]("time"))
      ),
      ok(jsonResponse[Appoint])
    )
  }

  val getNextEventId: Endpoint[Unit, Int] = {
    endpoint(
      get(
        root / "get-next-app-event-id"
      ),
      ok(jsonResponse[Int])
    )
  }

  val listAppEventSince: Endpoint[Int, List[AppEvent]] = {
    endpoint(
      get(
        root / "list-app-event-since" /? qs[Int]("from")
      ),
      ok(jsonResponse[List[AppEvent]])
    )
  }

  val listAppEventInRange: Endpoint[(Int, Int), List[AppEvent]] = {
    endpoint(
      get(
        root / "list-app-event-in-range" /? (qs[Int]("from") & qs[Int]("upto"))
      ),
      ok(jsonResponse[List[AppEvent]])
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

  implicit def dateTimeSchema(implicit
      string: JsonSchema[String]
  ): JsonSchema[LocalDateTime] =
    string.xmapPartial(toDateTime)(fromDateTime)

  implicit def dateParam(implicit
      string: QueryStringParam[String]
  ): QueryStringParam[LocalDate] =
    string.xmapPartial(toDate)(fromDate)

  implicit def timeParam(implicit
      string: QueryStringParam[String]
  ): QueryStringParam[LocalTime] =
    string.xmapPartial(toTime)(fromTime)

  implicit def dateTimeParam(implicit
      string: QueryStringParam[String]
  ): QueryStringParam[LocalDateTime] =
    string.xmapPartial(toDateTime)(fromDateTime)

  implicit lazy val appointSchema: JsonSchema[Appoint] = genericJsonSchema
  implicit lazy val appEventSchema: JsonSchema[AppEvent] = genericJsonSchema

  def toDate: String => Validated[LocalDate] = { s =>
    Try(LocalDate.parse(s)) match {
      case Success(date) => Valid(date)
      case Failure(_)    => Invalid(s"Invalid date value '${s}'")
    }
  }

  def fromDate: LocalDate => String = _.toString

  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  def toTime: String => Validated[LocalTime] = { s =>
    Try(LocalTime.parse(s, timeFormatter)) match {
      case Success(time) => Valid(time)
      case Failure(_)    => Invalid(s"Invalid time value '${s}'")
    }
  }

  def fromTime: LocalTime => String = _.format(timeFormatter)

  val dateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

  def fromDateTime: LocalDateTime => String = _.format(dateTimeFormatter)

  def toDateTime: String => Validated[LocalDateTime] = { s =>
    Try(LocalDateTime.parse(s, dateTimeFormatter)) match {
      case Success(dt) => Valid(dt)
      case Failure(_)  => Invalid(s"Invalid date time value '${s}'")
    }
  }

}
