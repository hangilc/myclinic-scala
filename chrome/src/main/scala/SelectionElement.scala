package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.By.ByClassName
import java.time.Duration
import scala.jdk.CollectionConverters.*

case class SelectionElement(e: WebElement, driver: WebDriver):
  def getVersion: Int = e.getAttribute("data-version").toInt
  def waitForVersion(ver: Int): Unit =
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until[Boolean](_ => getVersion == 1)
  def items: List[WebElement] =
    e.findElements(ByClassName("domq-selection-item")).asScala.toList
