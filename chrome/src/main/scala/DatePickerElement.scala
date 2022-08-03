package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ByCssSelector
import scala.jdk.CollectionConverters.*
import org.openqa.selenium.By.ByXPath

case class DatePickerElement(e: WebElement, driver: ChromeDriver):
  def yearSpan: WebElement = e.findElement(
    ByCssSelector("div.year-nen div.domq-date-picker-year-disp span")
  )
  def monthSpan: WebElement = e.findElement(
    ByCssSelector("div.year-nen div.domq-date-picker-month-disp span")
  )

  def chooseYear(year: Int): Unit =
    yearSpan.click()
    val yearList: WebElement = ElementUtil.waitFor(
      driver,
      ByCssSelector("body > div.domq-date-picker-year-list")
    )
    val items: List[WebElement] =
      yearList.findElements(ByClassName("domq-selection-item")).asScala.toList
    val t = s"(${year})"
    items.find(e => e.getText().contains(t)).get.click()
    ElementUtil.waitForDisappear(driver, yearList)

  def chooseMonth(month: Int): Unit =
    monthSpan.click()
    val monthList: WebElement = ElementUtil.waitFor(
      driver,
      ByCssSelector("body > div.domq-date-picker-month-list")
    )
    val items: List[WebElement] =
      monthList.findElements(ByClassName("domq-selection-item")).asScala.toList
    val t = String.format("%02d月", month)
    items.find(e => e.getText() == t).get.click()
    ElementUtil.waitForDisappear(driver, monthList)

  def chooseDay(day: Int): Unit =
    val de: WebElement = ElementUtil.waitFor(
      driver,
      e,
      ByXPath(
        "//*[@class='domq-date-picker-dates-tab']" + 
        "//*[@class='domq-date-picker-date-box' and not(@class='pre-month') and not(@class='post-month')" +
        s" and @data-date='${day}']"
        //".domq-date-picker-dates-tab .domq-date-picker-date-box:not(.pre-month):not(.post-month)"
      )
    )
    de.click()
    println(("choose-day", day))
    ElementUtil.waitForDisappear(driver, e)

object DatePickerElement:
  def apply(driver: ChromeDriver): DatePickerElement =
    val className = "domq-date-picker"
    val e: WebElement =
      ElementUtil.waitFor(driver, driver, ByClassName(className))
    new DatePickerElement(e, driver)
