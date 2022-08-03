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
import dev.myclinic.scala.client.MyClient

class Tester(
    baseUri: Uri,
    headless: Boolean
):
  val client = MyClient(baseUri)
  confirmTesting()
  val opts = new ChromeOptions()
  if headless then opts.addArguments("--headless")
  val driver: ChromeDriver = new ChromeDriver(opts)

  def open(url: String): Unit =
    println(("open", url))
    driver.get(url)
  def close(): Unit = driver.quit()

  def isTesting: Boolean =
    client.findPatient(1).fold(false)(p =>
      p.lastName == "Shinryou" && p.firstName == "Tarou"
    )

  def confirmTesting(): Unit =
    if !isTesting then throw new RuntimeException("Not a testing server")

  def confirm(bool: Boolean): Unit =
    if !bool then throw new RuntimeException("Failed to confirm")


