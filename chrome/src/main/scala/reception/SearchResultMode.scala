package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By
import scala.jdk.CollectionConverters.*

case class SearchResultMode(e: WebElement, driver: ChromeDriver):
  def selections: List[WebElement] = e
    .findElements(
      By.cssSelector(
        "div.domq-modal-dialog3-body div.domq-selection div.domq-selection-item"
      )
    )
    .asScala
    .toList
  def closeButton: WebElement = ElementUtil.getButtonByText(e, "閉じる")

  def searchResultTexts: List[String] =
    selections.map(_.getText)

  def close(): Unit = closeButton.click()

object SearchResultMode:
  val className = "search-result-mode"

  def apply(dialog: WebElement, driver: ChromeDriver): SearchResultMode =
    val e = ElementUtil.waitFor(driver, dialog, ByClassName(className))
    new SearchResultMode(dialog, driver)
