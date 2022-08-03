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
import io.circe.Encoder
import io.circe.Decoder
import org.http4s.blaze.client.BlazeClientBuilder

case class MyClient(
    baseUri: Uri = Uri.unsafeFromString("http://localhost:8080")
):
  import MyClient.*
  val baseApiUri: Uri = removeEndingSlash(baseUri / "api")
  val myApiUri = new MyApiUri(baseApiUri)

  def run[T](f: Api => IO[T]): T =
    BlazeClientBuilder[IO].resource.use( client =>
      val api = new Api(client, myApiUri)
      f(api)
    ).unsafeRunSync()

object MyClient:
  def addEndingSlash(uri: Uri): Uri =
    uri.withPath(uri.path.addEndsWithSlash)

  def removeEndingSlash(uri: Uri): Uri =
    uri.withPath(uri.path.dropEndsWithSlash)

  def addPathWithEndingSlash(uri: Uri, path: String): Uri =
    uri.withPath(uri.path.addSegment(path).addEndsWithSlash)

  def landingPage(baseUri: Uri, prog: String): Uri =
    addPathWithEndingSlash(baseUri, prog)

  def appointAppLandingPage(baseUri: Uri): Uri =
    landingPage(baseUri, "appoint")

  def receptionAppLandingPage(baseUri: Uri): Uri =
    landingPage(baseUri, "reception")

  def practiceAppLandingPage(baseUri: Uri): Uri =
    landingPage(baseUri, "practice")

class MyApiUri(baseApiUri: Uri):
  private def apiUri[K: QueryParamKeyLike, V: QueryParamEncoder](
      command: String,
      params: Map[K, V]
  ): Uri =
    baseApiUri.addSegment(command).withQueryParams(params)

  def getPatient(patientId: Int): Uri =
    apiUri("get-patient", Map("patient-id" -> patientId))

class Api(client: Client[IO], apiUri: MyApiUri):
  private def request[T: Decoder, B: Encoder](
      method: Method,
      uri: Uri,
      body: Option[B]
  ): Request[IO] =
    val req = Request[IO](method, uri)
    body.fold(req)(b => req.withEntity(b))

  private def get[T: Decoder](uri: Uri): IO[T] =
    client.expect[T](request[T, Unit](Method.GET, uri, None))

  private def post[T: Decoder, B: Encoder](
      uri: Uri,
      body: B
  ): IO[T] =
    client.expect[T](request[T, B](Method.POST, uri, Some(body)))

  def getPatient(patientId: Int): IO[Patient] =
    get[Patient](apiUri.getPatient(patientId))

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
    val appoint2 =
      appointTimes.find(_.fromTime == at.fromTime.plusMinutes(30)).get
    updateAppointTime(appoint1.copy(capacity = 7))
    updateAppointTime(appoint2.copy(capacity = 7))
