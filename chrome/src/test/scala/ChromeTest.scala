package test

import org.scalatest.funsuite.FixtureAnyFunSuite
import org.scalatest.Outcome
import dev.myclinic.scala.chrome.DriverFactory
import cats.instances.try_
import org.openqa.selenium.chrome.ChromeDriver

abstract class ChromeTest() extends FixtureAnyFunSuite:
  def createParam(factory: DriverFactory): FixtureParam

  override protected def withFixture(test: OneArgTest): Outcome = 
    val factory: DriverFactory = new DriverFactory()
    val driver: ChromeDriver = factory.driver
    val param = createParam(factory)
    try
      withFixture(test.toNoArgTest(param))
    finally 
      driver.close()
      driver.quit()
