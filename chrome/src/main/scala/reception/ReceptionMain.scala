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
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.SelectionElement.apply
import dev.myclinic.scala.chrome.SelectionElement

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

    def selectRecentVisits: RecentVisits =
      select("最近の診察")
      RecentVisits(driver)

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

  case class SearchPatient(e: WebElement, driver: ChromeDriver):
    val input: WebElement = e.findElement(ByTagName("input"))
    val select: WebElement = e.findElement(ByClassName("domq-selection"))

    def getVersion: Int = select.getAttribute("data-version").toInt    
    def waitForVersion(ver: Int): Unit =
      val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
      wait.until[Boolean](
        _ => getVersion == 1
      )
    def items: List[WebElement] =
      select.findElements(ByClassName("domq-selection-item")).asScala.toList

  object SearchPatient:
    def apply(driver: ChromeDriver): SearchPatient =
      val e = ElementUtil.waitFor(
        driver,
        ByCssSelector("body > div.domq-floating-element div.records-search-patient-box")
      )
      SearchPatient(e, driver)

  case class RecentVisits(e: WebElement, driver: ChromeDriver):
    def selection = SelectionElement(e, driver)

  object RecentVisits:
    def apply(driver: ChromeDriver): RecentVisits =
      val e = ElementUtil.waitFor(
        driver,
        ByCssSelector("body > div.domq-floating-element div.records-recent-visit-box")
      )
      RecentVisits(e, driver)

  case class ByDate(e: WebElement, driver: ChromeDriver)

  object ByDate:
    def apply(driver: ChromeDriver): ByDate =
      val e = ElementUtil.waitFor(
        driver,
        ByCssSelector("body > div.domq-floating-element div.records-select-by-date-box")
      )
      ByDate(e, driver)

object ReceptionMain:
  val className = "reception-cashier-service"

  def apply(driver: ChromeDriver): ReceptionMain =
    val e = ElementUtil.waitFor(driver, ByClassName(className))
    new ReceptionMain(e, driver)
