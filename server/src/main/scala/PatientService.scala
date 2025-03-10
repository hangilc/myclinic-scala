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
import dev.fujiwara.kanjidate.DateUtil
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import org.http4s.websocket.WebSocketFrame.Text
import dev.myclinic.scala.appoint.admin.AppointAdmin

object PatientService:

  object intPatientId extends QueryParamDecoderMatcher[Int]("patient-id")
  object strText extends QueryParamDecoderMatcher[String]("text")
  val digitsPattern = "[0-9]+".r

  def routes = HttpRoutes.of[IO] {
    case GET -> Root / "get-patient" :? intPatientId(patientId) =>
      Ok(Db.getPatient(patientId))

    case GET -> Root / "find-patient" :? intPatientId(patientId) =>
      Ok(Db.findPatient(patientId))

    case GET -> Root / "search-patient" :? strText(text) =>
      Ok(Db.searchPatient(text))

    case GET -> Root / "search-patient-by-phone" :? strText(text) =>
      Ok(Db.searchPatientByPhone(text))

    case req @ POST -> Root / "batch-get-patient" =>
      Ok(
        for
          patientIds <- req.as[List[Int]]
          map <- Db.batchGetPatient(patientIds)
        yield map
      )

    case GET -> Root / "list-patient-by-onshi-name" :? strText(text) =>
      Ok(Db.listPatientByOnshiName(text))
  }