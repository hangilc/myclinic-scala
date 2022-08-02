package dev.myclinic.scala.chrome

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.http4s.client.Client
import org.http4s.dsl.{io => _}
import org.http4s.client.dsl.io.*
import org.http4s.implicits.*
import org.http4s.circe.*
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.circe.CirceEntityEncoder.*
import cats.effect.*
import cats.syntax.all.*
import org.http4s.blaze.client.*
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import java.time.LocalDate
import cats.effect.unsafe.implicits.global
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import io.circe.syntax.*
import org.http4s.Uri
import org.http4s.Method
import org.http4s.Request
import org.http4s.QueryParamKeyLike
import org.http4s.QueryParamEncoder
import io.circe.Encoder
import io.circe.Decoder

class Tester(
    baseUri: Uri,
    headless: Boolean
):
  confirmTesting()
  val opts = new ChromeOptions()
  if headless then opts.addArguments("--headless")
  val driver: ChromeDriver = new ChromeDriver(opts)

  def open(url: String): Unit =
    println(("open", url))
    driver.get(url)
  def close(): Unit = driver.quit()

  def apiUrl(path: String): String =
    s"${baseUri}/api${path}"

  val apiBaseUri: Uri = baseUri.copy(
    path = baseUri.path.addSegment(Uri.Path.Segment("api")).dropEndsWithSlash
  )

  def apiUri(command: String): Uri = apiBaseUri / command

  def rest[T](f: Client[IO] => IO[T]): T =
    BlazeClientBuilder[IO].resource.use(f).unsafeRunSync()

  def apiGetUri[K: QueryParamKeyLike, V: QueryParamEncoder](
      command: String,
      params: Map[K, V]
  ): Uri =
    apiUri(command).withQueryParams(params)

  def get[T: Decoder](client: Client[IO], uri: Uri): IO[T] =
    client.expect[T](uri)

  def getPatient(client: Client[IO], patientId: Int): IO[Patient] =
    get[Patient](
      client,
      apiGetUri("get-patient", Map("patient-id" -> patientId))
    )

  def findPatient(client: Client[IO], patientId: Int): IO[Option[Patient]] =
    get[Option[Patient]](
      client,
      apiGetUri("find-patient", Map("patient-id" -> patientId))
    )

  def findPatient(patientId: Int): Option[Patient] =
    rest[Option[Patient]](findPatient(_, patientId))

  def isTesting: Boolean =
    findPatient(1).fold(false)(p =>
      p.lastName == "Shinryou" && p.firstName == "Tarou"
    )

  def confirmTesting(): Unit =
    if !isTesting then throw new RuntimeException("Not a testing server")

  def confirm(bool: Boolean): Unit =
    if !bool then throw new RuntimeException("Failed to confirm")

  def enterPatient(patient: Patient): Patient =
    val req = Request[IO](
      method = Method.POST,
      uri = Uri.unsafeFromString(apiUrl("/enter-patient"))
    )
      .withEntity(patient)
    rest[Patient](client => client.expect[Patient](req))

  def searchPatient(text: String): List[Patient] =
    rest[List[Patient]](
      _.expect[List[Patient]](apiUri(s"/search-patient?text=${text}"))
    )
