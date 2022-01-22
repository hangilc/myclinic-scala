package dev.myclinic.scala.client

import cats.*
import cats.syntax.*
import cats.effect.*
import cats.effect.implicits.*
import cats.effect.unsafe.implicits.global
import org.http4s.client.*
import org.http4s.Uri
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.*
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import java.time.{LocalDate, LocalTime}
import org.http4s.EntityDecoder

object Client:
  val client: Client[IO] = JavaNetClientBuilder[IO].withoutSslSocketFactory.create
  val uri: Uri = Uri.fromString(System.getenv("MYCLINIC_SCALA_SERVER"))
    .getOrElse(throw new RuntimeException("Cannot find server url."))
  def get[T](service: String, mod: Uri => Uri = uri => uri)(using EntityDecoder[IO, T]): T =
    client.expect[T](mod(uri / "api"/ service)).unsafeRunSync()

  def getPatient(patientId: Int): Patient =
    get[Patient]("get-patient", _.withQueryParam("patient-id", patientId))

  def listAppointTimes(from: LocalDate, upto: LocalDate): List[AppointTime] =
    get("list-appoint-times", _.withQueryParam("from", from.toString).withQueryParam("upto", upto.toString))

  def filterModerna(at: AppointTime): Boolean =
    at.kind == "covid-vac-moderna" 
    && at.fromTime == java.time.LocalTime.of(16, 40, 0)
    && at.untilTime == java.time.LocalTime.of(17, 20, 0)
    
