package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import dev.myclinic.scala.chrome.ElementBase
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions

class PatientDialog(e: WebElement) extends ElementBase(e):
  def patientId: String =
    val value =
      e.findElement(By.xpath("//span[text()='患者番号']/following-sibling::span"))
    value.getText

  def close(): Unit =
    findButtonByText("閉じる").click()

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
