package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By
import org.openqa.selenium.By.ByLinkText

case class PatientDispMode(e: WebElement, driver: ChromeDriver):
  def patientIdField: WebElement =
    e.findElement(By.xpath("//span[text()='患者番号']/following-sibling::span"))
  def closeButton: WebElement = ElementUtil.getButtonByText(e, "閉じる")
  def editLink: WebElement = e.findElement(ByLinkText("編集"))
  def newShahokokuhoLink: WebElement = e.findElement(ByLinkText("新規社保国保"))
  def newKoukikoureiLink: WebElement = e.findElement(ByLinkText("新規後期高齢"))
  def newKouhiLink: WebElement = e.findElement(ByLinkText("新規公費"))
  def hokenHistoryLink: WebElement = e.findElement(ByLinkText("保険履歴"))

  def patientId: String = patientIdField.getText

  def close(): Unit =
    closeButton.click()

object PatientDispMode:
  val className = "reception-cashier-patient-search-result-dialog-disp-body"

  def apply(dialog: WebElement, driver: ChromeDriver): PatientDispMode =
    val e = ElementUtil.waitFor(driver, dialog, ByClassName(className))
    new PatientDispMode(dialog, driver)
