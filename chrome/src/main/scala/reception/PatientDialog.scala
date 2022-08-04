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

class PatientDialog(e: WebElement, driver: ChromeDriver):
  def asSearchResult = SearchResultMode(e, driver)
  def asPatientDisp = PatientDispMode(e, driver)

object PatientDialog:
  val patientDialogClass = "reception-cashier-search-patient-result-dialog"
  def apply(driver: ChromeDriver): PatientDialog =
    val e = ElementUtil.waitFor(driver, ByClassName(patientDialogClass))
    new PatientDialog(e, driver)
