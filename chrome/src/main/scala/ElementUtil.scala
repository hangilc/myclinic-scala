package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.WebDriver
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions

object ElementUtil:
  def getButtonByText(wrapper: WebElement, text: String): WebElement =
    wrapper.findElement(By.xpath(s"//button[text()='${text}']"))

  def waitFor(driver: WebDriver, wrapper: SearchContext, locator: By): WebElement =
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until[WebElement](
      ExpectedConditions.visibilityOfElementLocated(locator)
    )

  def waitFor(driver: WebDriver, locator: By): WebElement =
    waitFor(driver, driver, locator)

  def waitForDisappear(driver: WebDriver, e: WebElement): Unit =
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until(
      ExpectedConditions.invisibilityOf(e)
    )

    
    

