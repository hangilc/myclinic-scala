package test

import org.scalatest.funsuite.AnyFunSuite
import dev.myclinic.scala.chrome.DriverFactory
import org.openqa.selenium.chrome.ChromeDriver
import org.scalatest.BeforeAndAfterAll

abstract class TestBase extends AnyFunSuite with BeforeAndAfterAll:
  val factory: DriverFactory = new DriverFactory()
  val driver: ChromeDriver = factory.driver

  override def afterAll(): Unit =
    driver.close()
    driver.quit()
