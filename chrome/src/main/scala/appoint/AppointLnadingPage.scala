package dev.myclinic.scala.chrome.appoint

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.DriverFactory
import org.openqa.selenium.By.ByTagName
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ByCssSelector
import dev.myclinic.scala.client.MyClient
import scala.jdk.CollectionConverters.*

case class AppointLandingPage(e: WebElement, client: MyClient, driver: ChromeDriver):
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

  def findOccupiedSlot: AppointSlotElement =
    val es = driver.findElements(ByClassName("appoint-slot")).asScala.toList
    new AppointSlotElement(es(0))

object AppointLandingPage:
  def apply(factory: DriverFactory): AppointLandingPage =
    val driver: ChromeDriver = factory.driver
    factory.driver.get(factory.appointLandingPage.toString)
    ElementUtil.waitUntil(driver, driver => 
      driver.findElements(ByClassName("appoint-column")).size() == 5  
    )
    AppointLandingPage(driver.findElement(ByTagName("body")), factory.client, driver)

