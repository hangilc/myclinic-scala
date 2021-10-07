package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.modeljson.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions

object Api:
  def url(service: String): String = s"/api/${service}"

  def get[T](service: String, params: Params)(using
      Decoder[T]
  ): Future[T] =
    Ajax.request("GET", url(service), params, "")

  def post[B, T](service: String, params: Params, body: B)(using
      Encoder[B],
      Decoder[T]
  ): Future[T] =
    Ajax.request("POST", url(service), params, body.asJson.toString())

  def hello(): Future[String] =
    get("hello", Params())

  def listAppointTimes(
      from: LocalDate,
      upto: LocalDate
  ): Future[List[AppointTime]] =
    get("list-appoint-times", Params("from" -> from, "upto" -> upto))

  def registerAppoint(appoint: Appoint): Future[Appoint] =
    post("register-appoint", Params(), appoint)

  def listAppointsForAppointTime(appointTimeId: Int): Future[List[Appoint]] =
    get(
      "list-appoints-for-appoint-time",
      Params("appoint-time-id" -> appointTimeId)
    )

  def listAppointsForDate(date: LocalDate): Future[List[Appoint]] =
    get("list-appoints-for-date", Params("date" -> date))

  def getNextAppEventId(): Future[Int] =
    get("get-next-app-event-id", Params())

  def listAppEventSince(fromEventId: Int): Future[List[AppEvent]] =
    get("list-app-event-since", Params("from" -> fromEventId))

  def listAppEventInRange(fromEventId: Int, untilEventId: Int): Future[List[AppEvent]] =
    get("list-app-event-in-range", Params("from" -> fromEventId, "upto" -> untilEventId))

// def listAppoint(from: LocalDate, upto: LocalDate): Future[List[Appoint]] =
//   get("list-appoint", Params("from" -> from, "upto" -> upto))

// def registerAppoint(appoint: Appoint): Future[String] =
//   post("register-appoint", Params(), appoint)

// def cancelAppoint(date: LocalDate, time: LocalTime, patientName: String): Future[String] =
//   post("cancel-appoint", Params("date" -> date, "time" -> time, "name" -> patientName), "")

// def getAppoint(date: LocalDate, time: LocalTime): Future[Appoint] =
//   get("get-appoint", Params("date" -> date, "time" -> time))
