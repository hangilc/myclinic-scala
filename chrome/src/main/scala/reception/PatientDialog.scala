package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import scala.jdk.CollectionConverters.*
import dev.myclinic.scala.chrome.ElementUtil

class PatientDialog(e: WebElement):
  def patientId: String =
    val value =
      e.findElement(By.xpath("//span[text()='患者番号']/following-sibling::span"))
    value.getText

  def close(): Unit =
    ElementUtil.getButtonByText(e, "閉じる").click()

  def searchResultTexts: List[String] =
    e.findElements(By.cssSelector("div.domq-modal-dialog3-body div.domq-selection div.domq-selection-item"))
      .asScala
      .toList
      .map(_.getText)

object PatientDialog:
  def apply(driver: ChromeDriver): PatientDialog =
    val className = "reception-cashier-search-patient-result-dialog"
    val wait = new WebDriverWait(driver, Duration.ofSeconds(2))
    wait.until(
      ExpectedConditions.visibilityOfElementLocated(
        ByClassName(className)
      )
    )
    val e = driver.findElement(
      ByClassName(className)
    )
    new PatientDialog(e)
