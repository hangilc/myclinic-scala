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
import org.openqa.selenium.By.ByXPath
import dev.myclinic.scala.model.Sex

case class NewPatientDialog(e: WebElement, driver: ChromeDriver):
  def lastNameInput: WebElement = e.findElement(ByClassName("last-name-input"))
  def firstNameInput: WebElement =
    e.findElement(ByClassName("first-name-input"))
  def lastNameYomiInput: WebElement =
    e.findElement(ByClassName("last-name-yomi-input"))
  def firstNameYomiInput: WebElement =
    e.findElement(ByClassName("first-name-yomi-input"))
  def birthdayDatePickerIcon: WebElement =
    e.findElement(ByCssSelector("div.birthday-input svg.domq-calendar-icon"))
  def maleRadio: WebElement =
    e.findElement(ByXPath(
      "//div[contains(@class, 'sex-input')]" 
      + "//label[contains(text(), '男')]"
      + "/preceding-sibling::input[@type='radio']"
    ))
  def femaleRadio: WebElement =
    e.findElement(ByXPath(
      "//div[contains(@class, 'sex-input')]" 
      + "//label[contains(text(), '女')]"
      + "/preceding-sibling::input[@type='radio']"
    ))
  def addressInput: WebElement =
    e.findElement(ByCssSelector("input.address-input"))
  def phoneInput: WebElement =
    e.findElement(ByCssSelector("input.phone-input"))
  def enterButton: WebElement = ElementUtil.getButtonByText(e, "入力")
  def cancelButton: WebElement = ElementUtil.getButtonByText(e, "キャンセル")

  def openBirthdayDatePicker(): DatePickerElement =
    birthdayDatePickerIcon.click()
    DatePickerElement(driver)

  def setSex(sex: Sex): Unit =
    sex match {
      case Sex.Male => maleRadio.click()
      case Sex.Female => femaleRadio.click()
    }

  def setInputs(patient: Patient): Unit =
    lastNameInput.sendKeys(patient.lastName)
    firstNameInput.sendKeys(patient.firstName)
    lastNameYomiInput.sendKeys(patient.lastNameYomi)
    firstNameYomiInput.sendKeys(patient.firstNameYomi)
    val picker: DatePickerElement = openBirthdayDatePicker()
    picker.set(patient.birthday)
    setSex(patient.sex)
    addressInput.sendKeys(patient.address)
    phoneInput.sendKeys(patient.phone)

  def enter(): Unit =
    enterButton.click()
    ElementUtil.waitForDisappear(driver, e)
  
  def cancel(): Unit =
    cancelButton.click()
    ElementUtil.waitForDisappear(driver, e)

object NewPatientDialog:
  def apply(driver: ChromeDriver): NewPatientDialog =
    val className = "reception-new-patient-dialog"
    val e: WebElement =
      ElementUtil.waitFor(driver, driver, ByClassName(className))
    new NewPatientDialog(e, driver)
