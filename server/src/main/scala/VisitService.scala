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
import dev.myclinic.scala.server.Publisher

object VisitService extends Publisher:
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case GET -> Root / "get-visit" :? intVisitId(visitId) => 
      Ok(Db.getVisit(visitId))

    case req @ POST -> Root / "batch-get-visit" => 
      Ok(for
        visitIds <- req.as[List[Int]]
        map <- Db.batchGetVisit(visitIds)
      yield map)

    case GET -> Root / "delete-visit" :? intVisitId(visitId) =>
      val op = {
        for
          events <- Db.deleteVisit(visitId)
          _ <- publishAll(events)
        yield true
      }
      Ok(op)

    case GET -> Root / "get-visit-ex" :? intVisitId(visitId) =>
      Ok(Db.getVisitEx(visitId))

    case req @ POST -> Root / "update-hoken-ids" :? intVisitId(visitId) =>
      val op = 
        for
          hokenIdSet <- req.as[HokenIdSet]
          event <- Db.updateHokenIds(visitId, hokenIdSet)
          _ <- publish(event)
        yield true
      Ok(op)

    case GET -> Root / "get-hoken-info" :? intVisitId(visitId) =>
      Ok(Db.getHokenInfo(visitId))

    case req @ POST -> Root / "update-visit" =>
      val op =
        for
          visit <- req.as[Visit]
          event <- Db.updateVisit(visit)
          _ <- publish(event)
        yield true
      Ok(op)

  }