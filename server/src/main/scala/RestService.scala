package dev.myclinic.scala.server

import cats.effect._
import cats.syntax.all._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.db.Db
import java.time.LocalDate
import java.time.LocalTime
import dev.myclinic.scala.util.DateUtil
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import org.http4s.websocket.WebSocketFrame.Text
import dev.myclinic.scala.appoint.admin.AppointAdmin
import java.time.LocalDateTime

object RestService extends DateTimeQueryParam with Publisher:

  object dateFrom extends QueryParamDecoderMatcher[LocalDate]("from")
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")
  object dateUpto extends QueryParamDecoderMatcher[LocalDate]("upto")
  object dateAt extends QueryParamDecoderMatcher[LocalDate]("at")
  object timeTime extends QueryParamDecoderMatcher[LocalTime]("time")
  object timeAt extends QueryParamDecoderMatcher[LocalTime]("at")
  object strName extends QueryParamDecoderMatcher[String]("name")
  object strRecipient extends QueryParamDecoderMatcher[String]("recipient")
  object strText extends QueryParamDecoderMatcher[String]("text")
  object strText1 extends QueryParamDecoderMatcher[String]("text1")
  object strText2 extends QueryParamDecoderMatcher[String]("text2")
  object intFrom extends QueryParamDecoderMatcher[Int]("from")
  object intUntil extends QueryParamDecoderMatcher[Int]("until")
  object intAppointTimeId
      extends QueryParamDecoderMatcher[Int]("appoint-time-id")
  object intAppointId extends QueryParamDecoderMatcher[Int]("appoint-id")
  object intLimit extends QueryParamDecoderMatcher[Int]("limit")
  object intOffset extends QueryParamDecoderMatcher[Int]("offset")
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")

  case class UserError(message: String) extends Exception

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {

    case GET -> Root / "list-appoint-time-filled" :? dateDate(date) =>
      Ok(Db.listAppointTimeFilled(date))

    case GET -> Root / "list-appoint-times" :? dateFrom(from) +& dateUpto(
          upto
        ) =>
      Ok(Db.listAppointTimes(from, upto))

    case GET -> Root / "get-appoint-time" :? intAppointTimeId(appointTimeId) =>
      Ok(Db.getAppointTime(appointTimeId))

    case GET -> Root / "find-appoint-time" :? intAppointTimeId(appointTimeId) =>
      Ok(Db.findAppointTime(appointTimeId))

    case GET -> Root / "list-appoint-times-for-date" :? dateDate(date) =>
      Ok(Db.listAppointTimesForDate(date))

    case req @ POST -> Root / "add-appoint-time" => {
      Ok({
        for
          appointTime <- req.as[AppointTime]
          event <- Db.createAppointTime(appointTime)
          _ <- publish(event)
        yield true
      })
    }

    case req @ POST -> Root / "update-appoint-time" => {
      val op = for
        appointTime <- req.as[AppointTime]
        event <- Db.updateAppointTime(appointTime)
        _ <- publish(event)
      yield true
      Ok(op)
    }

    case POST -> Root / "delete-appoint-time" :? intAppointTimeId(
          appointTimeId
        ) =>
      val op = for
        event <- Db.deleteAppointTime(appointTimeId)
        _ <- publish(event)
      yield true
      Ok(op)

    case req @ POST -> Root / "combine-appoint-times" => {
      val op = for
        appointTimeIds <- req.as[List[Int]]
        events <- Db.combineAppointTimes(appointTimeIds)
        _ <- publishAll(events)
      yield (true)
      Ok(op)
    }

    case POST -> Root / "split-appoint-time" :? intAppointTimeId(
          appointTimeId
        ) +&
        timeAt(at) =>
      val op = for
        events <- Db.splitAppointTime(appointTimeId, at)
        _ <- publishAll(events)
      yield (true)
      Ok(op)

    case POST -> Root / "fill-appoint-times" :? dateFrom(from) +& dateUpto(
          upto
        ) =>
      val op = for
        events <- AppointAdmin.fillAppointTimesUpto(from, upto)
        _ <- publishAll(events)
      yield (true)
      Ok(op)

    case req @ POST -> Root / "register-appoint" => {
      val op = for
        appoint <- req.as[Appoint]
        result <- Db.addAppoint(appoint)
        (entered, appEvent) = result
        _ <- publish(appEvent)
      yield entered
      Ok(op)
    }

    case GET -> Root / "get-appoint" :? intAppointId(appointId) =>
      Ok(Db.getAppoint(appointId))

    case req @ POST -> Root / "update-appoint" =>
      val op = for
        appoint <- req.as[Appoint]
        event <- Db.updateAppoint(appoint)
        _ <- publish(event)
      yield true
      Ok(op)

    case POST -> Root / "cancel-appoint" :? intAppointId(appointId) => {
      val op = for
        event <- Db.cancelAppoint(appointId)
        _ <- publish(event)
      yield true
      Ok(op)
    }

    case GET -> Root / "list-appoints-for-appoint-time" :? intAppointTimeId(
          appointTimeId
        ) => {
      Ok(Db.listAppointsForAppointTime(appointTimeId))
    }

    case GET -> Root / "list-appoints-for-date" :? dateDate(date) => {
      Ok(Db.listAppointsForDate(date))
    }

    case GET -> Root / "get-next-app-event-id" => Ok(Db.nextGlobalEventId())

    case GET -> Root / "list-app-event-since" :? intFrom(from) => {
      Ok(Db.listGlobalEventSince(from))
    }

    case GET -> Root / "list-app-event-in-range" :? intFrom(from) +& intUntil(
          until
        ) => {
      Ok(Db.listGlobalEventInRange(from, until))
    }

    case GET -> Root / "list-appoint-events" :? intLimit(limit) +& intOffset(
          offset
        ) =>
      Ok(Db.listAppointEvents(limit, offset))

    case GET -> Root / "appoint-history-at" :? intAppointTimeId(
          appointTimeId
        ) =>
      Ok(Db.appointHistoryAt(appointTimeId))

    case GET -> Root / "search-appoint-by-patient-name" :? strText(text) =>
      Ok(Db.searchAppointByPatientName(text))

    case GET -> Root / "search-appoint-by-patient-name2" :? strText1(
          text1
        ) +& strText2(text2) =>
      Ok(Db.searchAppointByPatientName2(text1, text2))

    case GET -> Root / "search-appoint-by-patient-id" :? intPatientId(
          patientId
        ) =>
      Ok(Db.searchAppointByPatientId(patientId))

    case req @ POST -> Root / "post-hotline" =>
      val op =
        for
          hotline <- req.as[Hotline]
          event <- Db.postHotline(hotline)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "hotline-beep" :? strRecipient(recipient) =>
      Ok(for _ <- publish(HotlineBeep(recipient)) yield true)

    case GET -> Root / "list-todays-hotline" =>
      Ok(Db.listTodaysHotline())

    case GET -> Root / "list-wqueue" => Ok(Db.listWqueue())

    case GET -> Root / "list-wqueue-full" => Ok(Db.listWqueueFull())

    case GET -> Root / "find-wqueue-full" :? intVisitId(visitId) =>
      Ok(Db.findWqueueFull(visitId))

    case GET -> Root / "find-wqueue" :? intVisitId(visitId) =>
      Ok(Db.findWqueue(visitId))

    case req @ POST -> Root / "update-wqueue" =>
      val op =
        for
          wq <- req.as[Wqueue]
          event <- Db.updateWqueue(wq)
          _ <- publish(event)
        yield true
      Ok(op)

    case req @ POST -> Root / "enter-patient" =>
      Ok {
        for
          patient <- req.as[Patient]
          event <- Db.enterPatient(patient)
          _ <- publish(event)
        yield true
      }

    case req @ POST -> Root / "update-patient" =>
      Ok {
        for
          patient <- req.as[Patient]
          event <- Db.updatePatient(patient)
          _ <- publish(event)
        yield true
      }

    case GET -> Root / "get-visit-patient" :? intVisitId(visitId) =>
      Ok(Db.getVisitPatient(visitId))

    case GET -> Root / "get-patient-hoken" :? intPatientId(patientId) +& dateAt(
          at
        ) =>
      Ok(Db.getPatientHoken(patientId, at))

    case GET -> Root / "get-patient-all-hoken" :? intPatientId(patientId) =>
      Ok(Db.getPatientAllHoken(patientId))

    case req @ POST -> Root / "batch-resolve-shinryoucode-by-name" :? dateAt(
          at
        ) =>
      val op =
        for
          names <- req.as[List[String]]
          codes <-
            names
              .map(name => Helper.findShinryoucodeByName(name, at))
              .sequence
              .map(opts => opts.map(_.getOrElse(9)))
        yield Map(names.zip(codes): _*)
      Ok(op)

  } <+> PatientService.routes <+> VisitService.routes <+> MiscService.routes
    <+> ConfigService.routes <+> FileService.routes
    <+> DrawerService.routes
