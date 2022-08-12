package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.DriverFactory
import org.openqa.selenium.By.ByTagName
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ByCssSelector
import dev.myclinic.scala.client.MyClient
import scala.jdk.CollectionConverters.*
import org.openqa.selenium.support.ui.ExpectedConditions
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.model.Sex
import java.time.LocalDate

class ReceptionLandingPage(e: WebElement, client: MyClient, driver: ChromeDriver):
  def searchTextInput: WebElement =
    driver.findElement(ByClassName("reception-cashier-search-text-input"))

  def searchButton: WebElement =
    driver.findElement(ByClassName("reception-cashier-search-button"))

  def newPatientButton: WebElement =
    driver.findElement(ByClassName("reception-cashier-new-patient-button"))

  def cashierMenuIcon: WebElement =
    e.findElement(ByClassName("reception-cashier-menu-icon"))

  def openCashierMenu: CashierMenu =
    cashierMenuIcon.click()
    CashierMenu(driver)

  def wqueueTable: WqueueTable =
    WqueueTable(driver, e)

object ReceptionLandingPage:
  def apply(factory: DriverFactory): ReceptionLandingPage =
    val driver: ChromeDriver = factory.driver
    factory.driver.get(factory.receptionLandingPage.toString)
    ElementUtil.waitFor(driver,
      ByClassName("reception-cashier-wqueue-table")
    )
    new ReceptionLandingPage(driver.findElement(ByTagName("body")), factory.client, driver)

  def ensureSearchPatients(client: MyClient): Unit =
    val patients: List[Patient] = client.searchPatient("Test Number")
    def exists(i: Int): Boolean =
      patients
        .find(p => p.lastName == "Test" && p.firstName == s"Number${i}")
        .isDefined
    def mkPatient(i: Int): Patient =
      Patient(
        0,
        "Test",
        s"Number${i}",
        "",
        "",
        Sex.Male,
        LocalDate.of(2000, 1, 1),
        "",
        ""
      )
    Range(2, 5).foreach(i =>
      if !exists(i) then client.enterPatient(mkPatient(i))
    )
