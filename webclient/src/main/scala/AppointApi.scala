package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.modeljson.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions

object AppointApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def listAppointTimes(
        from: LocalDate,
        upto: LocalDate
    ): Future[List[AppointTime]] =
      get("list-appoint-times", Params("from" -> from, "upto" -> upto))

    def updateAppointTime(appointTime: AppointTime): Future[Unit] =
      post("update-appoint-time", Params(), appointTime)

    def listAppointTimesForDate(date: LocalDate): Future[List[AppointTime]] =
      get("list-appoint-times-for-date", Params("date" -> date))

    def fillAppointTimes(from: LocalDate, upto: LocalDate): Future[Boolean] =
      post("fill-appoint-times", Params("from" -> from, "upto" -> upto))

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
      get("list-app-event-in-range", Params("from" -> fromEventId, "until" -> untilEventId))

    def getAppoint(appointId: Int): Future[Appoint] =
      get("get-appoint", Params("appoint-id" -> appointId))

    def updateAppoint(appoint: Appoint): Future[Boolean] =
      post("update-appoint", Params(), appoint)

    def cancelAppoint(appointId: Int): Future[Unit] =
      post("cancel-appoint", Params("appoint-id" -> appointId))

    def combineAppointTimes(appointTimeIds: List[Int]): Future[Unit] =
      post("combine-appoint-times", Params(), appointTimeIds)

    def splitAppointTime(appointTimeId: Int, at: LocalTime): Future[Unit] =
      post("split-appoint-time", Params("appoint-time-id" -> appointTimeId, "at" -> at))  