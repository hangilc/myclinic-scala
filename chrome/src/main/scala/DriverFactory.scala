package dev.myclinic.scala.chrome

import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.client.MyClient
import org.http4s.Uri
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.chrome.ChromeDriverLogLevel

class DriverFactory:
  val baseUri: Uri = resolveBaseUri
  val headless: Boolean = resolveHeadless
  val client: MyClient = MyClient(baseUri)
  confirmTesting()

  lazy val driver: ChromeDriver =
    val opts = new ChromeOptions()
    if resolveHeadless then opts.addArguments("--headless")
    opts.setLogLevel(ChromeDriverLogLevel.WARNING)
    println("driver created")
    new ChromeDriver(opts)

  def resolveBaseUri: Uri =
    val envVal: String = System.getenv("MYCLINIC_TEST_SERVER")
    val loc: String =
      if envVal == null || envVal.isEmpty then "http://localhost:8080" else envVal
    Uri.unsafeFromString(loc)

  def appointLandingPage: Uri =
    MyClient.appointAppLandingPage(baseUri)

  def receptionLandingPage: Uri =
    MyClient.receptionAppLandingPage(baseUri)

  def practiceLandingPage: Uri =
    MyClient.practiceAppLandingPage(baseUri)

  def resolveHeadless: Boolean =
    val envVal: String = System.getenv("MYCLINIC_CHROME_HEADLESS")
    envVal == null || envVal.toUpperCase == "TRUE"    

  def isTesting: Boolean =
    client.findPatient(1).fold(false)(p =>
      p.lastName == "Shinryou" && p.firstName == "Tarou"
    )

  def confirmTesting(): Unit =
    if !isTesting then throw new RuntimeException("Not a testing server")

    
