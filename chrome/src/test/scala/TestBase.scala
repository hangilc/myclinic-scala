package test

import org.scalatest.funsuite.AnyFunSuite
import dev.myclinic.scala.chrome.DriverFactory
import org.openqa.selenium.chrome.ChromeDriver
import org.scalatest.BeforeAndAfterAll
import dev.myclinic.scala.client.MyClient

abstract class TestBase extends AnyFunSuite with BeforeAndAfterAll:
  val factory: DriverFactory = new DriverFactory()
  val driver: ChromeDriver = factory.driver
  val client: MyClient = factory.client

  override def afterAll(): Unit =
    driver.close()
    driver.quit()

  def confirm(bool: Boolean): Unit =
    if !bool then fail()
