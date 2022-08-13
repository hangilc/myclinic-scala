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
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Sex
import java.time.LocalDate
import org.openqa.selenium.support.pagefactory.ByChained
import org.openqa.selenium.By.ByXPath

class WqueueTable(e: WebElement, driver: ChromeDriver):
  def waitForRow(visitId: Int): WqueueTable.Row =
    val ele = ElementUtil.waitFor(driver, e, 
      ByCssSelector(s"div.reception-cashier-wqueue-table-row[data-visit-id='${visitId}']")
    )
    new WqueueTable.Row(ele, driver)

  def waitForRowWithCashierButton(visitId: Int): WqueueTable.Row =
    ElementUtil.waitFor(driver, e,
      ByChained(
        ByCssSelector(s"div.reception-cashier-wqueue-table-row[data-visit-id='${visitId}']"),
        ByXPath("//button[text()='会計']")
      )
    )
    waitForRow(visitId)

object WqueueTable:
  def apply(driver: ChromeDriver, wrapper: WebElement): WqueueTable =
    val e = ElementUtil.waitFor(driver, wrapper, ByClassName("reception-cashier-wqueue-table"))
    new WqueueTable(e, driver)

  class Row(val e: WebElement, driver: ChromeDriver):
    def cashierButton: WebElement = 
      ElementUtil.getButtonByText(e, "会計")

