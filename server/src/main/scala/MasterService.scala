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
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given

object MasterService extends DateTimeQueryParam:

  object intIyakuhincode extends QueryParamDecoderMatcher[Int]("iyakuhincode")
  object dateAt extends QueryParamDecoderMatcher[LocalDate]("at")

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "get-iyakuhin-master" :? intIyakuhincode(iyakuhincode) +& dateAt(at) =>
      Ok(Db.getIyakuhinMaster(iyakuhincode, at))

    case req @ POST -> Root / "batch-get-iyakuhin-master" :? dateAt(at) =>
      Ok(for 
        codes <- req.as[List[Int]]
        map <- Db.batchResolveIyakuhinMaster(codes, at)
      yield map)
  }