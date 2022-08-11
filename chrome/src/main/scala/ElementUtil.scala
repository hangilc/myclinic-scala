package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.WebDriver
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.By.ByClassName
import scala.jdk.CollectionConverters.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.By.ByCssSelector

object ElementUtil:
  def getButtonByText(wrapper: WebElement, text: String): WebElement =
    wrapper.findElement(By.xpath(s"//button[text()='${text}']"))

  def waitUntil(
    driver: WebDriver,
    f: WebDriver => Boolean
  ): Unit =
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until[Boolean](driver => f(driver))

  def waitFor(
      driver: WebDriver,
      wrapper: SearchContext,
      locator: By
  ): WebElement =
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until[WebElement](
      _ => wrapper.findElement(locator)
    )

  def waitFor(driver: WebDriver, locator: By): WebElement =
    waitFor(driver, driver, locator)

  def waitForDisappear(driver: WebDriver, e: WebElement): Unit =
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until(
      ExpectedConditions.invisibilityOf(e)
    )

  def waitForModalDialog(driver: WebDriver, className: String): WebElement =
    waitFor(driver, ByCssSelector(s"body > div.domq-modal-dialog-content.${className}"))

  def xpathContainsClass(className: String): String =
    s"contains(concat(' ', @class, ' '), ' ${className} ')"

  def topZIndexOf(eles: List[WebElement]): WebElement =
    eles
      .sortBy(e =>
        e.getCssValue("z-index") match {
          case "auto" => -1
          case z      => z.toInt
        }
      )
      .reverse
      .last

  def topDomqScreen(driver: ChromeDriver): WebElement =
    println(("screens", driver.findElements(ByClassName("domq-screen")).size))
    topZIndexOf(driver.findElements(ByClassName("domq-screen")).asScala.toList)

  def closePullDown(e: WebElement, driver: ChromeDriver): Unit =
    ElementUtil.topDomqScreen(driver).click()
    ElementUtil.waitForDisappear(driver, e)

  def closeByButton(e: WebElement, driver: ChromeDriver): Unit =
    getButtonByText(e, "閉じる").click()
    waitForDisappear(driver, e)
