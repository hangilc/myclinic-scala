package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.support.pagefactory.ByChained
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByXPath
import org.openqa.selenium.By.ByLinkText
import org.openqa.selenium.By.ByTagName

case class ReceptionMain(e: WebElement, driver: ChromeDriver):
  val headBox: WebElement = e.findElement(ByClassName("reception-cashier-head-box"))

  val recordLink: WebElement = headBox.findElement(ByChained(
    ByCssSelector("a.domq-pull-down"),
    ByXPath("self::*[text()='診療録']")
  ))

  def openRecordMenu: RecordMenuItems =
    recordLink.click()
    RecordMenuItems(driver)

  case class RecordMenuItems(e: WebElement, driver: ChromeDriver):
    def select(name: String): Unit =
      e.findElement(ByLinkText(name)).click()

    def close(): Unit =
      driver.findElement(ByTagName("html")).click()
      ElementUtil.waitForDisappear(driver, e)

  object RecordMenuItems:
    val className = "record-menu-items"

    def apply(driver: ChromeDriver): RecordMenuItems =
      val e = ElementUtil.waitFor(driver, ByClassName(className))
      new RecordMenuItems(e, driver)

object ReceptionMain:
  val className = "reception-cashier-service"

  def apply(driver: ChromeDriver): ReceptionMain =
    val e = ElementUtil.waitFor(driver, ByClassName(className))
    new ReceptionMain(e, driver)

