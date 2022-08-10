package test

import dev.myclinic.scala.client.MyClient
import dev.myclinic.scala.chrome.DriverFactory
import dev.myclinic.scala.chrome.appoint.AppointLandingPage
import org.openqa.selenium.chrome.ChromeDriver
import dev.myclinic.scala.chrome.appoint.*
import org.openqa.selenium.By.ByClassName
import dev.myclinic.scala.chrome.ElementUtil
import org.openqa.selenium.By

class AppointTest extends ChromeTest:
  val page: AppointLandingPage = AppointLandingPage(factory)

  test("testing enter"){
    val box = page.findVacantAppointBox
    val prevSlots = box.slots.length
    box.e.click()
    val dlog = new MakeAppointDialogElement(
      driver.findElement(ByClassName("domq-modal"))
    )
    dlog.insertName("1\n")
    dlog.enterButton.click()
    ElementUtil.waitUntil(driver, drv => box.slots.length == prevSlots + 1)
  }

  test("testing cancel"){
    val slot = page.findOccupiedSlot
    slot.e.click()
    driver
      .findElement(
        By.xpath("//div[@class='domq-modal']//button[text()='予約取消実行']")
      )
      .click()
    driver
      .findElement(By.xpath("//div[@class='domq-modal']//button[text()='はい']"))
      .click()
    ElementUtil.waitForDisappear(driver, slot.e)
  }