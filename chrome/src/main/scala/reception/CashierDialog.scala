package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByLinkText
import org.openqa.selenium.support.pagefactory.ByChained
import org.openqa.selenium.By.ByXPath

class CashierDialog(e: WebElement, driver: ChromeDriver):
  def printReceiptButton: WebElement =
    ElementUtil.getButtonByText(e, "領収書印刷")
  def finishCashierButton: WebElement =
    ElementUtil.getButtonByText(e, "会計終了")

object CashierDialog:
  def apply(driver: ChromeDriver): CashierDialog =
    val e = ElementUtil.waitFor(driver, 
      ByChained(
        ByCssSelector("body > div.domq-modal div.cashier-dialog"),
        ByXPath("./parent::div")
      )
    )
    new CashierDialog(e, driver)
