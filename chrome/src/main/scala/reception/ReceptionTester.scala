package dev.myclinic.scala.chrome.reception

import org.openqa.selenium.chrome.ChromeDriver
import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.*
import org.http4s.blaze.client.*
import cats.effect.unsafe.implicits.global
import org.http4s.circe.CirceEntityDecoder.*
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import io.circe.Json
import scala.concurrent.Future
import org.openqa.selenium.By.ByCssSelector
import scala.jdk.CollectionConverters.*
import java.time.LocalDate
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import scala.concurrent.duration.Duration.apply
import java.time.Duration
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions
import dev.myclinic.scala.chrome.Config
import dev.myclinic.scala.chrome.Tester
import org.http4s.Uri
import dev.myclinic.scala.client.MyClient
import dev.myclinic.scala.chrome.TestUtil
import java.time.LocalDateTime

class ReceptionTester(
    baseUri: Uri = Config.baseUrl,
    headless: Boolean = Config.headless
) extends Tester(baseUri, headless):
  open(MyClient.receptionAppLandingPage(baseUri).toString)

  def searchTextInput: WebElement =
    driver.findElement(ByClassName("reception-cashier-search-text-input"))

  def searchButton: WebElement =
    driver.findElement(ByClassName("reception-cashier-search-button"))

  def newPatientButton: WebElement =
    driver.findElement(ByClassName("reception-cashier-new-patient-button"))

  def testAll(): Unit =
    testSearchPatient()
    testSearchPatientMulti()
    testNewPatient()

  def testSearchPatient(): Unit =
    searchTextInput.sendKeys("1\n")
    val patientDisp = PatientDialog(driver).asPatientDisp
    confirm(patientDisp.patientId == "1")
    patientDisp.close()
    println("OK: search patient")

  def testSearchPatientMulti(): Unit =
    ensureSearchPatients()
    searchTextInput.sendKeys("Test Number")
    searchButton.click()
    val mode = PatientDialog(driver).asSearchResult
    val texts: List[String] = mode.searchResultTexts
    confirm(Range(2, 5).toList.forall(i =>
      val t = s"Test Number${i}"
      texts.find(_.contains(t)).isDefined
    ))
    mode.close()
    println("OK: search patient multi")

  def testNewPatient(): Unit =
    newPatientButton.click()
    val dlog = NewPatientDialog(driver)
    val i: Int = TestUtil.randomInt(0, 999)
    val lastName = String.format("Number%03d", i)
    val p = Patient(
      0,
      "TestNew",
      lastName,
      "TestNewYomi",
      s"${lastName}Yomi",
      TestUtil.randomSex,
      LocalDate.of(
        TestUtil.randomInt(1950, 2010),
        TestUtil.randomInt(1, 12),
        TestUtil.randomInt(1, 28)
      ),
      TestUtil.randomString(12),
      TestUtil.randomPhone
    )
    dlog.setInputs(p)
    dlog.enter()
    val searched: List[Patient] = client.searchPatient(
      s"${p.lastName} ${p.firstName}"
    )
    confirm(!searched.isEmpty)
    val searchedLastPatientId: Int = searched.map(_.patientId).max
    val searchedLast: Patient = searched.find(_.patientId == searchedLastPatientId).get
    confirm(p.copy(patientId = searchedLast.patientId) == searchedLast)
    val disp = PatientDialog(driver).asPatientDisp
    disp.close()
    println("OK: new patient")

  def testRecordMenu(): Unit =
    testRecordMenuFromWqueue()
    testRecordMenuSearchPatient()

  def testRecordMenuFromWqueue(): Unit =
    if client.listWqueue.isEmpty then
      client.startVisit(1, LocalDateTime.now())
    val main = ReceptionMain(driver)
    val menu = main.openRecordMenu
    val fromWqueue = menu.selectFromWqueue
    fromWqueue.patientLinks(0).click()
    val record = RecordDialog(driver)
    record.close()
    println("OK: record menu (from wqueue)")

  def testRecordMenuSearchPatient(): Unit =
    if client.countVisitByPatient(1) == 0 then
      client.startVisit(1, LocalDateTime.now())
    val main = ReceptionMain(driver)
    val menu = main.openRecordMenu
    val searchPatient = menu.selectBySearchPatient
    println("OK: record menu (search patient)")
    


  def ensureSearchPatients(): Unit =
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
