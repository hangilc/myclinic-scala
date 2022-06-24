package dev.myclinic.scala.client

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.*
import org.http4s.client.*
import org.http4s.client.dsl.io.*
import org.http4s.implicits.*
import org.http4s.Uri
import org.http4s.EntityEncoder.*
import org.http4s.Method.*
import java.util.concurrent.*
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import org.http4s.circe.{*, given}
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import java.time.{LocalDate, LocalTime}
import org.http4s.{EntityDecoder, EntityEncoder}
import dev.fujiwara.kanjidate.DateUtil

object Client:
  val client: Client[IO] =
    JavaNetClientBuilder[IO].withoutSslSocketFactory.create
  val uri: Uri = Uri
    .fromString(System.getenv("MYCLINIC_SCALA_SERVER"))
    .getOrElse(throw new RuntimeException("Cannot find server url."))
  given QueryParamEncoder[LocalTime] with
    def encode(value: LocalTime): QueryParameterValue =
      new QueryParameterValue(DateUtil.timeToString(value))
  def get[T](service: String, mod: Uri => Uri = uri => uri)(using
      EntityDecoder[IO, T]
  ): T =
    client.expect[T](mod(uri / "api" / service)).unsafeRunSync()
  def post[B, T](
      service: String,
      body: B,
      mod: Uri => Uri = identity[Uri]
  )(using EntityEncoder[IO, B], EntityDecoder[IO, T]): T =
    client
      .expect[T](
        POST(
          body,
          mod(uri / "api" / service)
        )
      )
      .unsafeRunSync()

  def getPatient(patientId: Int): Patient =
    get[Patient]("get-patient", _.withQueryParam("patient-id", patientId))

  def listAppointTimes(from: LocalDate, upto: LocalDate): List[AppointTime] =
    get(
      "list-appoint-times",
      _.withQueryParam("from", from.toString)
        .withQueryParam("upto", upto.toString)
    )

  def listAppointTimes(date: LocalDate): List[AppointTime] =
    listAppointTimes(date, date)

  def updateAppointTime(at: AppointTime): Boolean =
    post("update-appoint-time", at)

  def splitAppointTime(appointTime: AppointTime, at: LocalTime): Boolean =
    post(
      "split-appoint-time",
      "",
      _.withQueryParam("appoint-time-id", appointTime.appointTimeId)
        .withQueryParam("at", at)
    )

  def isModerna(at: AppointTime): Boolean =
    at.kind == "covid-vac-moderna"
      && at.fromTime == java.time.LocalTime.of(16, 40, 0)
      && (at.untilTime == java.time.LocalTime.of(
        17,
        20,
        0
      ) || at.untilTime == java.time.LocalTime.of(17, 40, 0))

  def transformModerna(at: AppointTime): Unit =
    if !isModerna(at) then
      throw new RuntimeException(s"Invalid appoint time: ${at}")
    val modified = at.copy(untilTime = LocalTime.of(17, 40, 0))
    updateAppointTime(modified)
    splitAppointTime(at, at.fromTime.plusMinutes(30))
    val appointTimes = listAppointTimes(at.date)
    val appoint1 = appointTimes.find(_.fromTime == at.fromTime).get
    val appoint2 = appointTimes.find(_.fromTime == at.fromTime.plusMinutes(30)).get
    updateAppointTime(appoint1.copy(capacity = 7))
    updateAppointTime(appoint2.copy(capacity = 7))
