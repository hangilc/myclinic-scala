package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByCssSelector

case class RecordDialog(e: WebElement, driver: ChromeDriver):
  def close(): Unit = ElementUtil.closeByButton(e, driver)

object RecordDialog:
  def apply(driver: ChromeDriver): RecordDialog =
    val e = ElementUtil.waitFor(driver, ByCssSelector(
      "body > div.domq-modal-dialog-content.reception-cashier-record-dialog"
    ))
    RecordDialog(e, driver)
