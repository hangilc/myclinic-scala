package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.By.ByCssSelector

class PrintDialog(e: WebElement, driver: ChromeDriver):
  def cancelButton: WebElement = 
    ElementUtil.getButtonByText(e, "キャンセル")

  def cancel(): Unit =
    cancelButton.click()
    ElementUtil.waitForDisappear(driver, e)

object PrintDialog:
  def apply(driver: ChromeDriver): PrintDialog =
    val e = ElementUtil.waitFor(driver, driver,
      ByCssSelector("body > div.domq-modal.print-dialog"),
      10
    )
    new PrintDialog(e, driver)