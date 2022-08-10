package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.DriverFactory
import org.openqa.selenium.By.ByTagName
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ByCssSelector
import dev.myclinic.scala.client.MyClient
import scala.jdk.CollectionConverters.*
import org.openqa.selenium.support.ui.ExpectedConditions

class ReceptionLandingPage(e: WebElement, client: MyClient, driver: ChromeDriver)

object ReceptionLandingPage:
  def apply(factory: DriverFactory): ReceptionLandingPage =
    val driver: ChromeDriver = factory.driver
    factory.driver.get(factory.receptionLandingPage.toString)
    ElementUtil.waitFor(driver,
      ByClassName("reception-cashier-wqueue-table")
    )
    new ReceptionLandingPage(driver.findElement(ByTagName("body")), factory.client, driver)

