package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import dev.myclinic.scala.model.Patient
import org.openqa.selenium.By.ByCssSelector
import dev.myclinic.scala.chrome.DatePickerElement
import org.openqa.selenium.WebDriver

case class NewPatientDialog(e: WebElement, driver: ChromeDriver):
  def enterButton: WebElement = ElementUtil.getButtonByText(e, "入力")
  def cancelButton: WebElement = ElementUtil.getButtonByText(e, "キャンセル")
  def lastNameInput: WebElement = e.findElement(ByClassName("last-name-input"))
  def firstNameInput: WebElement =
    e.findElement(ByClassName("first-name-input"))
  def lastNameYomiInput: WebElement =
    e.findElement(ByClassName("last-name-yomi-input"))
  def firstNameYomiInput: WebElement =
    e.findElement(ByClassName("first-name-yomi-input"))
  def birthdayDatePickerIcon: WebElement =
    e.findElement(ByCssSelector("div.birthday-input svg.domq-calendar-icon"))

  def openBirthdayDatePicker(): DatePickerElement =
    birthdayDatePickerIcon.click()
    DatePickerElement(driver)

  def setInputs(patient: Patient): Unit =
    lastNameInput.sendKeys(patient.lastName)
    firstNameInput.sendKeys(patient.firstName)
    lastNameYomiInput.sendKeys(patient.lastNameYomi)
    firstNameYomiInput.sendKeys(patient.firstNameYomi)
    val picker: DatePickerElement = openBirthdayDatePicker()
    picker.chooseYear(patient.birthday.getYear)
    picker.chooseMonth(patient.birthday.getMonthValue)
    picker.chooseDay(patient.birthday.getDayOfMonth)

object NewPatientDialog:
  def apply(driver: ChromeDriver): NewPatientDialog =
    val className = "reception-new-patient-dialog"
    val e: WebElement =
      ElementUtil.waitFor(driver, driver, ByClassName(className))
    new NewPatientDialog(e, driver)
