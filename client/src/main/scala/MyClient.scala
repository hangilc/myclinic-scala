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

  def run[T](f: MyRequest => IO[T]): T =
    BlazeClientBuilder[IO].resource
      .use(client =>
        val myReq = MyRequest(baseApiUri, client)
        f(myReq)
      )
      .unsafeRunSync()

  def getPatient(patientId: Int): Patient =
    run[Patient](_.getPatient(patientId))
  def findPatient(patientId: Int): Option[Patient] =
    run[Option[Patient]](_.findPatient(patientId))

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

class MyRequest(baseApiUri: Uri, client: Client[IO]):
  private def apiUri(
      command: String
  ): Uri =
    baseApiUri.addSegment(command)

  private def apiUri[K: QueryParamKeyLike, V: QueryParamEncoder](
      command: String,
      params: Map[K, V]
  ): Uri =
    apiUri(command).withQueryParams(params)

  private def get[K: QueryParamKeyLike, V: QueryParamEncoder](
      command: String,
      params: Map[K, V]
  ): Request[IO] =
    Request[IO](Method.GET, apiUri(command, params))

  private def post[B: Encoder](
      command: String,
      body: B
  ): Request[IO] =
    Request[IO](Method.POST, apiUri(command)).withEntity(body)

  private def post[K: QueryParamKeyLike, V: QueryParamEncoder, B: Encoder](
      command: String,
      params: Map[K, V],
      body: B
  ): Request[IO] =
    Request[IO](Method.POST, apiUri(command, params)).withEntity(body)

  private def run[T: Decoder](req: Request[IO]): IO[T] =
    client.expect[T](req)

  def getPatient(patientId: Int): IO[Patient] =
    run[Patient](get("get-patient", Map("patient-id" -> patientId)))

  def findPatient(patientId: Int): IO[Option[Patient]] =
    run[Option[Patient]](get("find-patient", Map("patient-id" -> patientId)))

  def enterPatient(patient: Patient): IO[Patient] =
    run[Patient](post("enter-patient", patient))

// object Client:
//   val client: Client[IO] =
//     JavaNetClientBuilder[IO].withoutSslSocketFactory.create
//   val uri: Uri = Uri
//     .fromString(System.getenv("MYCLINIC_SCALA_SERVER"))
//     .getOrElse(throw new RuntimeException("Cannot find server url."))
//   given QueryParamEncoder[LocalTime] with
//     def encode(value: LocalTime): QueryParameterValue =
//       new QueryParameterValue(DateUtil.timeToString(value))
//   def get[T](service: String, mod: Uri => Uri = uri => uri)(using
//       EntityDecoder[IO, T]
//   ): T =
//     client.expect[T](mod(uri / "api" / service)).unsafeRunSync()
//   def post[B, T](
//       service: String,
//       body: B,
//       mod: Uri => Uri = identity[Uri]
//   )(using EntityEncoder[IO, B], EntityDecoder[IO, T]): T =
//     client
//       .expect[T](
//         POST(
//           body,
//           mod(uri / "api" / service)
//         )
//       )
//       .unsafeRunSync()

//   def getPatient(patientId: Int): Patient =
//     get[Patient]("get-patient", _.withQueryParam("patient-id", patientId))

//   def listAppointTimes(from: LocalDate, upto: LocalDate): List[AppointTime] =
//     get(
//       "list-appoint-times",
//       _.withQueryParam("from", from.toString)
//         .withQueryParam("upto", upto.toString)
//     )

//   def listAppointTimes(date: LocalDate): List[AppointTime] =
//     listAppointTimes(date, date)

//   def updateAppointTime(at: AppointTime): Boolean =
//     post("update-appoint-time", at)

//   def splitAppointTime(appointTime: AppointTime, at: LocalTime): Boolean =
//     post(
//       "split-appoint-time",
//       "",
//       _.withQueryParam("appoint-time-id", appointTime.appointTimeId)
//         .withQueryParam("at", at)
//     )

//   def isModerna(at: AppointTime): Boolean =
//     at.kind == "covid-vac-moderna"
//       && at.fromTime == java.time.LocalTime.of(16, 40, 0)
//       && (at.untilTime == java.time.LocalTime.of(
//         17,
//         20,
//         0
//       ) || at.untilTime == java.time.LocalTime.of(17, 40, 0))

//   def transformModerna(at: AppointTime): Unit =
//     if !isModerna(at) then
//       throw new RuntimeException(s"Invalid appoint time: ${at}")
//     val modified = at.copy(untilTime = LocalTime.of(17, 40, 0))
//     updateAppointTime(modified)
//     splitAppointTime(at, at.fromTime.plusMinutes(30))
//     val appointTimes = listAppointTimes(at.date)
//     val appoint1 = appointTimes.find(_.fromTime == at.fromTime).get
//     val appoint2 =
//       appointTimes.find(_.fromTime == at.fromTime.plusMinutes(30)).get
//     updateAppointTime(appoint1.copy(capacity = 7))
//     updateAppointTime(appoint2.copy(capacity = 7))
