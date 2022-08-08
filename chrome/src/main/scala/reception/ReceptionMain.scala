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
import scala.jdk.CollectionConverters.*
import org.openqa.selenium.WebDriver

case class ReceptionMain(e: WebElement, driver: ChromeDriver):
  val headBox: WebElement =
    e.findElement(ByClassName("reception-cashier-head-box"))

  val recordLink: WebElement = headBox.findElement(
    ByChained(
      ByCssSelector("a.domq-pull-down"),
      ByXPath("self::*[text()='診療録']")
    )
  )

  def openRecordMenu: RecordMenuItems =
    recordLink.click()
    RecordMenuItems(driver)

  case class RecordMenuItems(e: WebElement, driver: ChromeDriver):
    def select(name: String): Unit =
      e.findElement(ByLinkText(name)).click()

    def selectFromWqueue: FromWqueue =
      select("受付患者")
      FromWqueue(driver)

    def selectBySearchPatient: SearchPatient =
      select("患者検索")
      SearchPatient(driver)

    def close(): Unit =
      ElementUtil.topDomqScreen(driver).click()
      ElementUtil.waitForDisappear(driver, e)

  object RecordMenuItems:
    val className = "record-menu-items"

    def apply(driver: ChromeDriver): RecordMenuItems =
      val e = ElementUtil.waitFor(driver, ByClassName(className))
      new RecordMenuItems(e, driver)

  case class FromWqueue(e: WebElement, driver: ChromeDriver):
    def close(): Unit =
      ElementUtil.closePullDown(e, driver)

    def patientLinks: List[WebElement] =
      e.findElements(ByCssSelector("a")).asScala.toList

  object FromWqueue:
    def apply(driver: ChromeDriver): FromWqueue =
      val e = ElementUtil.waitFor(
        driver,
        ByCssSelector("body > div.domq-floating-element div.wqueue-patient-list")
      )
      FromWqueue(e, driver)

  case class SearchPatient(e: WebElement, driver: ChromeDriver)

  object SearchPatient:
    def apply(driver: ChromeDriver): SearchPatient =
      val e = ElementUtil.waitFor(
        driver,
        ByCssSelector("body > div.domq-floating-element div.records-search-patient-box")
      )
      SearchPatient(e, driver)

object ReceptionMain:
  val className = "reception-cashier-service"

  def apply(driver: ChromeDriver): ReceptionMain =
    val e = ElementUtil.waitFor(driver, ByClassName(className))
    new ReceptionMain(e, driver)
