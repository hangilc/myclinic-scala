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
import org.openqa.selenium.By.ByClassName
import dev.myclinic.scala.model.Koukikourei

class KoukikoureiDialog(e: WebElement, driver: ChromeDriver):
  def hokenshaInput: WebElement = e.findElement(ByClassName("hokensha-input"))
  def hihokenshaInput: WebElement = e.findElement(ByClassName("hihokensha-input"))
  def futanWariInput(n: Int): WebElement = 
    import dev.myclinic.scala.util.ZenkakuUtil.Ext.*
    val label = s"${n}割".toZenkaku
    ElementUtil.getInputByLabel(e, label)
  def validFromCalendarIcon: WebElement =
    e.findElement(ByCssSelector("div.valid-from-input svg.domq-calendar-icon"))
  def validUptoCalendarIcon: WebElement =
    e.findElement(ByCssSelector("div.valid-upto-input svg.domq-calendar-icon"))
  def validUptoClearIcon: WebElement =
    e.findElement(ByCssSelector("div.valid-upto-input svg.domq-x-circle-icon"))
  def enterButton: WebElement = ElementUtil.getButtonByText(e, "入力")
  def cancelButton: WebElement = ElementUtil.getButtonByText(e, "キャンセル")

  def setFutanWari(f: Int): Unit =
    futanWariInput(f).click()

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
  def set(k: Koukikourei): Unit =
    hokenshaInput.clear()
    hokenshaInput.sendKeys(k.hokenshaBangou)
    hihokenshaInput.sendKeys(k.hihokenshaBangou)
    setFutanWari(k.futanWari)
    setValidFrom(k.validFrom)
    setValidUpto(k.validUpto)

  def enter(): Unit =
    enterButton.click()

  def cancel(): Unit = 
    cancelButton.click()

object KoukikoureiDialog:
  def apply(driver: ChromeDriver): KoukikoureiDialog =
    val e = ElementUtil.waitFor(
      driver,
      ByCssSelector(
        "body > div.domq-modal-dialog-content.reception-cashier-search-patient-result-dialog"
      )
    )
    ElementUtil.waitFor(
      driver,
      e,
      ByClassName("koukikourei-form")
    )
    new KoukikoureiDialog(e, driver)
