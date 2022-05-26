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
  object intShinryoucode extends QueryParamDecoderMatcher[Int]("shinryoucode")
  object intKizaicode extends QueryParamDecoderMatcher[Int]("kizaicode")
  object intVisitId extends QueryParamDecoderMatcher[Int]("visit-id")
  object intConductId extends QueryParamDecoderMatcher[Int]("conduct-id")
  object dateAt extends QueryParamDecoderMatcher[LocalDate]("at")

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "get-iyakuhin-master" :? intIyakuhincode(
          iyakuhincode
        ) +& dateAt(at) =>
      Ok(Db.getIyakuhinMaster(iyakuhincode, at))

    case req @ POST -> Root / "batch-get-iyakuhin-master" :? dateAt(at) =>
      Ok(for
        codes <- req.as[List[Int]]
        map <- Db.batchResolveIyakuhinMaster(codes, at)
      yield map)

    case GET -> Root / "get-shinryou-master" :? intShinryoucode(
          shinryoucode
        ) +& dateAt(at) =>
      Ok(Db.getShinryouMaster(shinryoucode, at))

    case req @ POST -> Root / "batch-resolve-shinryou-master" :? dateAt(at) =>
      Ok(for
        codes <- req.as[List[Int]]
        map <- Db.batchResolveShinryouMaster(codes, at)
      yield map)

    case GET -> Root / "get-kizai-master" :? intKizaicode(
          kizaicode
        ) +& dateAt(at) =>
      Ok(Db.getKizaiMaster(kizaicode, at))

    case req @ POST -> Root / "batch-get-kizai-master" :? dateAt(at) =>
      Ok(for
        codes <- req.as[List[Int]]
        map <- Db.batchResolveKizaiMaster(codes, at)
      yield map)

    case GET -> Root / "list-drug-for-visit" :? intVisitId(visitId) =>
      Ok(Db.listDrugForVisit(visitId))

    case GET -> Root / "list-shinryou-for-visit" :? intVisitId(visitId) =>
      Ok(Db.listShinryouForVisit(visitId))

    case GET -> Root / "list-conduct-for-visit" :? intVisitId(visitId) =>
      Ok(Db.listConductForVisit(visitId))

    case GET -> Root / "list-conduct-drug-for-visit" :? intConductId(conductId) =>
      Ok(Db.listConductDrugForConduct(conductId))

    case GET -> Root / "list-conduct-shinryou-for-visit" :? intConductId(conductId) =>
      Ok(Db.listConductShinryouForConduct(conductId))

    case GET -> Root / "list-conduct-kizai-for-visit" :? intConductId(conductId) =>
      Ok(Db.listConductKizaiForConduct(conductId))
  }
