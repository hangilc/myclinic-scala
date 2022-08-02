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

class ReceptionTester(baseUrl: String = Config.baseUrl, headless: Boolean = Config.headless)
  extends Tester(baseUrl, headless):
  open(baseUrl + "/reception/")
  def testSearchPatient(): Unit =
    driver.findElement(ByClassName("reception-cashier-search-text-input"))
      .sendKeys("1\n")
    val patientDialog = PatientDialog(driver)
    confirm(patientDialog.patientId == "1")
    patientDialog.close()
    println("OK: search patient")

  def ensureSearchPatients(): Unit =
    val tmpls: List[(Int, String, String)] =
      Range(2, 5).toList.map(i => (i, "Test", s"Number${i}"))
    tmpls.foreach{ (patientId, lastName, firstName) =>
      val pOpt = findPatient(patientId)
    }

