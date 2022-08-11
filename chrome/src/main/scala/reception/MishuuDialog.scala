package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByCssSelector

class MishuuDialog(e: WebElement, driver: ChromeDriver)

object MishuuDialog:
  def apply(driver: ChromeDriver): MishuuDialog =
    val e = ElementUtil.waitForModalDialog(driver, "mishuu-dialog")
    new MishuuDialog(e, driver)
