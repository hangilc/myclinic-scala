package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByLinkText
import org.openqa.selenium.support.pagefactory.ByChained
import org.openqa.selenium.By.ByXPath
import dev.myclinic.scala.model.Shahokokuho
import java.time.LocalDate
import dev.myclinic.scala.chrome.DatePickerElement.apply
import dev.myclinic.scala.chrome.DatePickerElement
import dev.myclinic.scala.model.ValidUpto

class ShahokokuhoDialog(e: WebElement, driver: ChromeDriver):
  def hokenshaBangouInput: WebElement = e.findElement(ByCssSelector("input.hokensha-bangou-input"))
  def hihokenshaKigouInput: WebElement = e.findElement(ByCssSelector("input.hihokensha-kigou-input"))
  def hihokenshaBangouInput: WebElement = e.findElement(ByCssSelector("input.hihokensha-bangou-input"))
  def edabanInput: WebElement = e.findElement(ByCssSelector("input.edaban-input"))
  def honninInput: WebElement = ElementUtil.getInputByLabel(e, "本人")
  def kazokuInput: WebElement = ElementUtil.getInputByLabel(e, "家族")
  def validFromCalendarIcon: WebElement =
    e.findElement(ByCssSelector("div.valid-from-input svg.domq-calendar-icon"))
  def validUptoCalendarIcon: WebElement =
    e.findElement(ByCssSelector("div.valid-upto-input svg.domq-calendar-icon"))
  def validUptoClearIcon: WebElement =
    e.findElement(ByCssSelector("div.valid-upto-input svg.domq-x-circle-icon"))
  def notKoureiInput: WebElement = ElementUtil.getInputByLabel(e, "高齢でない")
  def kourei1wariInput: WebElement = ElementUtil.getInputByLabel(e, "１割")
  def kourei2wariInput: WebElement = ElementUtil.getInputByLabel(e, "２割")
  def kourei3wariInput: WebElement = ElementUtil.getInputByLabel(e, "３割")
  def enterButton: WebElement = ElementUtil.getButtonByText(e, "入力")
  def cancelButton: WebElement = ElementUtil.getButtonByText(e, "キャンセル")

  def setValidFrom(d: LocalDate): Unit =
    validFromCalendarIcon.click()
    val p = DatePickerElement(driver)
    p.set(d)

  def setValidUpto(upto: ValidUpto): Unit =
    upto.value match {
      case Some(d) => 
        validUptoCalendarIcon.click()
        DatePickerElement(driver).set(d)
      case None =>
        val icon = validUptoClearIcon
        if icon.getCssValue("display") != "none" then
          icon.click()
    }

  def setKourei(kourei: Int): Unit =
    kourei match {
      case 0 => notKoureiInput.click()
      case 1 => kourei1wariInput.click()
      case 2 => kourei2wariInput.click()
      case 3 => kourei3wariInput.click()
      case _ => sys.error("Unknown kourei")
    }

  def set(shahokokuho: Shahokokuho): Unit =
    hokenshaBangouInput.sendKeys(shahokokuho.hokenshaBangou.toString)
    hihokenshaKigouInput.sendKeys(shahokokuho.hihokenshaKigou)
    hihokenshaBangouInput.sendKeys(shahokokuho.hihokenshaBangou)
    edabanInput.sendKeys(shahokokuho.edaban)
    if shahokokuho.isHonnin then
      honninInput.click()
    else
      kazokuInput.click()
    setValidFrom(shahokokuho.validFrom)
    setValidUpto(shahokokuho.validUpto)
    setKourei(shahokokuho.koureiStore)

  def enter(): Unit =
    enterButton.click()

  def cancel(): Unit =
    cancelButton.click()

object ShahokokuhoDialog:
  def apply(driver: ChromeDriver): ShahokokuhoDialog =
    val e = ElementUtil.waitFor(
      driver,
      ByCssSelector(
        "body > div.domq-modal-dialog-content.reception-cashier-search-patient-result-dialog"
      )
    )
    new ShahokokuhoDialog(e, driver)
