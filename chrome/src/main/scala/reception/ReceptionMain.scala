package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.support.pagefactory.ByChained
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByXPath
import org.openqa.selenium.By.ByLinkText

case class ReceptionMain(e: WebElement, driver: ChromeDriver):
  val headBox: WebElement = e.findElement(ByClassName("reception-cashier-head-box"))

  val recordLink: WebElement = headBox.findElement(ByChained(
    ByCssSelector("a.domq-pull-down"),
    ByXPath("self::*[text()='診療録']")
  ))

object ReceptionMain:
  val className = "reception-cashier-service"

  def apply(driver: ChromeDriver): ReceptionMain =
    val e = ElementUtil.waitFor(driver, ByClassName(className))
    new ReceptionMain(e, driver)

