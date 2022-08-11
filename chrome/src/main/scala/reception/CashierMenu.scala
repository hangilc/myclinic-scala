package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByLinkText

class CashierMenu(e: WebElement, driver: ChromeDriver):
  def link(text: String): WebElement = e.findElement(ByLinkText(text))

  def selectMishuuDialog: MishuuDialog =
    link("未収処理").click()
    MishuuDialog(driver)

object CashierMenu:
  def apply(driver: ChromeDriver): CashierMenu =
    val e = ElementUtil.waitFor(
      driver,
      ByCssSelector(
        "body > div.domq-context-menu.reception-cashier-context-menu"
      )
    )
    new CashierMenu(e, driver)
