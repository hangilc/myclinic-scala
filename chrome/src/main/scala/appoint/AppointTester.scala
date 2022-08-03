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
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions
import dev.myclinic.scala.chrome.Config
import dev.myclinic.scala.chrome.Tester
import org.http4s.Uri
import dev.myclinic.scala.client.MyClient

class AppointTester(
    baseUri: Uri = Config.baseUrl,
    headless: Boolean = Config.headless
) extends Tester(baseUri, headless):
  open(MyClient.appointAppLandingPage(baseUri).toString)
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

  def testCancel(): Unit =
    val slot = findOccupiedSlot
    slot.e.click()
    driver
      .findElement(
        By.xpath("//div[@class='domq-modal']//button[text()='予約取消実行']")
      )
      .click()
    driver
      .findElement(By.xpath("//div[@class='domq-modal']//button[text()='はい']"))
      .click()
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until(ExpectedConditions.invisibilityOf(slot.e))
    println("OK: cancel")

  def findOccupiedSlot: AppointSlotElement =
    val es = driver.findElements(ByClassName("appoint-slot")).asScala.toList
    new AppointSlotElement(es(0))

  def findVacantAppointBox: AppointTimeBoxElement =
    val cols = driver
      .findElements(ByCssSelector(".appoint-column.in-operation"))
      .asScala
      .toList
    val c = new AppointColumnElement(cols(0))
    if c.e.findElements(ByCssSelector(".appoint-time-box.vacant")).isEmpty then
      val d = c.date
      client.fillAppointTimes(d, d)
    new AppointTimeBoxElement(
      c.e.findElement(ByCssSelector(".appoint-time-box.vacant"))
    )

  // def fillAppointTimes(from: LocalDate, upto: LocalDate): Unit =
  //   rest(client =>
  //     client.expect[Boolean](
  //       apiUri("/fill-appoint-times" + params("from" -> from, "upto" -> upto))
  //     )
  //   )
