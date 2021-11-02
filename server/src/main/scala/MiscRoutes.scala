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
import dev.myclinic.scala.appoint.admin.AppointAdmin

object MiscService extends DateTimeQueryParam:
  object dateDate extends QueryParamDecoderMatcher[LocalDate]("date")

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case GET -> Root / "resolve-clinic-operation" :? dateDate(date) =>
      Ok(ClinicOperation.getClinicOperationAt(date))

  }