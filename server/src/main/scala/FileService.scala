package dev.myclinic.scala.server

import cats.*
import cats.syntax.*
import cats.effect.*
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.syntax._
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame

object FileService extends DateTimeQueryParam with Publisher:
  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object strFileName extends QueryParamDecoderMatcher[String]("file-name")

  def routes(using topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case req @ POST -> Root / "save-patient-image" :? intPatientId(patientId) +& strFileName(fileName) =>
      val op = for
        bytes <- req.as[Array[Byte]]
      yield 
        println(("bytes", bytes.size))
        true
      Ok(op)
  }