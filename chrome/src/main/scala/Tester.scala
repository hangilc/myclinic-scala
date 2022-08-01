package dev.myclinic.scala.chrome

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.http4s.client.Client
import org.http4s.circe.CirceEntityDecoder.*
import cats.effect.*
import cats.syntax.all.*
import org.http4s.blaze.client.BlazeClientBuilder
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import java.time.LocalDate
import cats.effect.unsafe.implicits.global
import org.openqa.selenium.WebElement
import org.openqa.selenium.By

class Tester(
    baseUrl: String,
    headless: Boolean
):
  confirmTesting()
  val opts = new ChromeOptions()
  if headless then opts.addArguments("--headless")
  val driver: ChromeDriver = new ChromeDriver(opts)

  def open(url: String): Unit = 
    driver.get(url)
  def close(): Unit = driver.quit()

  def apiUrl(path: String): String =
    s"${baseUrl}/api${path}"

  def rest[T](f: Client[IO] => IO[T]): T =
    BlazeClientBuilder[IO].resource.use(f).unsafeRunSync()

  def isTesting: Boolean =
    rest[Boolean] { client =>
      for
        patientOpt <- client.expect[Option[Patient]](
          apiUrl("/find-patient?patient-id=1")
        )
      yield patientOpt.fold(false)(p =>
        p.lastName == "Shinryou" && p.firstName == "Tarou"
      )
    }

  def confirmTesting(): Unit =
    if !isTesting then throw new RuntimeException("Not a testing server")

  type QueryValue = LocalDate

  def params(map: (String, QueryValue)*): String =
    "?" + map
      .map((key, value) =>
        val v = value match {
          case d: LocalDate => d.toString
        }
        s"${key}=${v}"
      )
      .mkString("&")

  def confirm(bool: Boolean): Unit =
    if !bool then throw new RuntimeException("Failed to confirm")


