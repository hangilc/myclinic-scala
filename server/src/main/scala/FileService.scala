package dev.myclinic.scala.server

import cats.*
import cats.syntax.*
import cats.effect.*
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.headers._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.syntax._
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import fs2.Chunk
import scodec.bits.ByteVector
import fs2.io.file.Path

object FileService extends DateTimeQueryParam with Publisher:
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object strFileName extends QueryParamDecoderMatcher[String]("file-name")

  private def saveToFile(req: Request[IO], path: Path): IO[Response[IO]] =
    req.body
      .through(fs2.io.file.Files[IO].writeAll(path))
      .compile
      .drain
      .map(_ =>
        Response[IO]()
          .withStatus(Status.Ok)
          .withEntity[Boolean](true)
          .withContentType(`Content-Type`(MediaType.application.json))
      )

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case req @ POST -> Root / "save-patient-image" :? intPatientId(
          patientId
        ) +& strFileName(fileName) =>
      saveToFile(req, Path("../image.jpg"))
  }
