package dev.myclinic.scala.chrome.appoint

import org.openqa.selenium.chrome.ChromeDriver
import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.*
import org.http4s.blaze.client.*
import cats.effect.unsafe.implicits.global
import org.http4s.circe.CirceEntityDecoder.*
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import io.circe.Json
import scala.concurrent.Future
import org.openqa.selenium.By.ByCssSelector
import scala.jdk.CollectionConverters.*
import java.time.LocalDate
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import scala.concurrent.duration.Duration.apply
import java.time.Duration

object Util:
  val baseUrl: String = "http://localhost:8080"

  def apiUrl(path: String): String =
    s"${baseUrl}/api${path}"

  def isTesting: Boolean =
    BlazeClientBuilder[IO].resource
      .use { client =>
        for
          patientOpt <- client.expect[Option[Patient]](
            apiUrl("/find-patient?patient-id=1")
          )
        yield patientOpt.fold(false)(p =>
          p.lastName == "Shinryou" && p.firstName == "Tarou"
        )
      }
      .unsafeRunSync()

  def rest[T](f: Client[IO] => IO[T]): T =
    BlazeClientBuilder[IO].resource.use(f).unsafeRunSync()

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

class AppointTester:
  import Util.*
  confirmTesting()
  val driver = new ChromeDriver()
  driver.get(s"${baseUrl}/appoint/")

  def close(): Unit =
    driver.close()

  def testEnter(): Unit =
    val box = findVacantAppointBox
    val prevSlots = box.slots.length
    box.e.click()
    val dlog = new MakeAppointDialogElement(
      driver.findElement(ByClassName("domq-modal"))
    )
    dlog.insertName("1\n")
    dlog.enterButton.click()
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until(drv => box.slots.length == prevSlots + 1)
    println("OK: testEnter")

  def findVacantAppointBox: AppointTimeBoxElement =
    val cols = driver
      .findElements(ByCssSelector(".appoint-column.in-operation"))
      .asScala
      .toList
    val c = new AppointColumnElement(cols(0))
    if c.e.findElements(ByCssSelector(".appoint-time-box.vacant")).isEmpty then
      val d = c.date
      fillAppointTimes(d, d)
    new AppointTimeBoxElement(
      c.e.findElement(ByCssSelector(".appoint-time-box.vacant"))
    )

  def fillAppointTimes(from: LocalDate, upto: LocalDate): Unit =
    rest(client =>
      client.expect[Boolean](
        apiUrl("/fill-appoint-times" + params("from" -> from, "upto" -> upto))
      )
    )
