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

object VisitService:
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "get-visit" :? intVisitId(visitId) => 
      Ok(Db.getVisit(visitId))

    case req @ POST -> Root / "batch-get-visit" => 
      Ok(for
        visitIds <- req.as[List[Int]]
        map <- Db.batchGetVisit(visitIds)
      yield map)
  }