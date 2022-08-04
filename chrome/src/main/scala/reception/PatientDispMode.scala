package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By

case class PatientDispMode(e: WebElement, driver: ChromeDriver):
  def patientIdField: WebElement =
    e.findElement(By.xpath("//span[text()='患者番号']/following-sibling::span"))
  def closeButton: WebElement = ElementUtil.getButtonByText(e, "閉じる")

  def patientId: String = patientIdField.getText

  def close(): Unit =
    closeButton.click()

object PatientDispMode:
  val className = "reception-cashier-patient-search-result-dialog-disp-body"

  def apply(dialog: WebElement, driver: ChromeDriver): PatientDispMode =
    val e = ElementUtil.waitFor(driver, dialog, ByClassName(className))
    new PatientDispMode(dialog, driver)
