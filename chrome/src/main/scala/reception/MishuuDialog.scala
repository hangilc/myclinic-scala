package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByClassName
import scala.jdk.CollectionConverters.*

class MishuuDialog(val e: WebElement, driver: ChromeDriver):

  def asSearch = MishuuDialog.Search(e, driver)
  def asShow = MishuuDialog.Show(e, driver)

object MishuuDialog:
  def apply(driver: ChromeDriver): MishuuDialog =
    val e = ElementUtil.waitForModalDialog(driver, "mishuu-dialog")
    new MishuuDialog(e, driver)

  class Search(e: WebElement, driver: ChromeDriver):
    def textInput: WebElement = e.findElement(
      ByCssSelector("div.records-search-patient-box form input[type='text']")
    )

  object Search:
    def apply(e: WebElement, driver: ChromeDriver): Search =
      ElementUtil.waitFor(driver, e, ByClassName("records-search-patient-box"))
      new Search(e, driver)

  class Show(e: WebElement, driver: ChromeDriver):
    def checkboxes: List[WebElement] = e.findElements(
      ByCssSelector("div.mishuu-items-wrapper input[type='checkbox']")
    ).asScala.toList
    def enterButton: WebElement = ElementUtil.getButtonByText(e, "会計に加える")

    def enter(): Unit =
      enterButton.click()
      ElementUtil.waitForDisappear(driver, e)

  object Show:
    def apply(e: WebElement, driver: ChromeDriver): Show =
      ElementUtil.waitFor(driver, e, ByClassName("mishuu-items-wrapper"))
      new Show(e, driver)


