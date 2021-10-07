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
import dev.myclinic.scala.modeljson.Implicits.{given}
import dev.myclinic.scala.db.Db
import java.time.LocalDate
import java.time.LocalTime
import dev.myclinic.scala.util.DateUtil
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import dev.myclinic.scala.model._
import org.http4s.websocket.WebSocketFrame.Text

object RestService:

  given QueryParamDecoder[LocalDate] =
    QueryParamDecoder[String].map(DateUtil.stringToDate(_))
  given QueryParamDecoder[LocalTime] =
    QueryParamDecoder[String].map(DateUtil.stringToTime(_))

  object dateFrom extends QueryParamDecoderMatcher[LocalDate]("from")
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")
  object dateUpto extends QueryParamDecoderMatcher[LocalDate]("upto")
  object timeTime extends QueryParamDecoderMatcher[LocalTime]("time")
  object nameString extends QueryParamDecoderMatcher[String]("name")
  object intFrom extends QueryParamDecoderMatcher[Int]("from")
  object intUntil extends QueryParamDecoderMatcher[Int]("until")
  object intAppointTimeId
      extends QueryParamDecoderMatcher[Int]("appoint-time-id")

  case class UserError(message: String) extends Exception

  def hello(): IO[String] =
    //throw new UserError("さようなら")
    "こんにちは".pure[IO]

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {

    case GET -> Root / "hello" => {
      try Ok(hello())
      catch
        case e: UserError => BadRequest(e.message, "X-User-Error" -> "true")
        case e: Exception => throw e

    }

    case GET -> Root / "list-appoint-times" :? dateFrom(from) +& dateUpto(
          upto
        ) =>
      Ok(Db.listAppointTimes(from, upto))

    case req @ POST -> Root / "register-appoint" => {
      val op = for
        appoint <- req.as[Appoint]
        result <- Db.addAppoint(appoint)
        (entered, appEvent) = result
        _ <- topic.publish1(Text(appEvent.asJson.toString()))
      yield entered
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

    case GET -> Root / "list-app-event-in-range" :? intFrom(from) +& intUntil(until) => {
      Ok(Db.listGlobalEventInRange(from, until))
    }

  // case req @ POST -> Root / "register-appoint" => {
    //   val op = for
    //     appoint <- req.as[Appoint]
    //     appEvent <- Db.registerAppoint(appoint)
    //     _ <- topic.publish1(Text(appEvent.asJson.toString()))
    //   yield "ok".asJson
    //   Ok(op)
    // }

    // case req @ POST -> Root / "cancel-appoint" :? dateDate(date) +& timeTime(
    //       time
    //     ) +& nameString(name) => {
    //   val op = for
    //     appEvent <- Db.cancelAppoint(date, time, name)
    //     _ <- topic.publish1(Text(appEvent.asJson.toString()))
    //   yield "ok".asJson
    //   Ok(op)
    // }

    // case GET -> Root / "get-appoint" :? dateDate(date) +& timeTime(time) => {
    //   Ok(Db.getAppoint(date, time))
    // }

    // case GET -> Root / "get-next-app-event-id" => {
    //   Ok(Db.nextGlobalEventId())
    // }


  }