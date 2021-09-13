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

object RestService {

  given QueryParamDecoder[LocalDate] =
    QueryParamDecoder[String].map(DateUtil.stringToDate(_))

  object dateFrom extends QueryParamDecoderMatcher[LocalDate]("from")
  object dateUpto extends QueryParamDecoderMatcher[LocalDate]("upto")

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {

    case GET -> Root / "hello" => Ok("\"hello, world\"")

    case GET -> Root / "list-appoint" :? dateFrom(from) +& dateUpto(upto) =>
      Ok(Db.listAppoint(from, upto))

    case req @ POST -> Root / "register-appoint" => {
        val op = for {
          appoint <- req.as[Appoint]
          appEvent <- Db.registerAppoint(appoint)
          _ <- topic.publish1(Text(appEvent.asJson.toString()))
        } yield "ok".asJson
        Ok(op)
    }
  }

}
