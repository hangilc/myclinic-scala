package dev.myclinic.scala.chrome.appoint

import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By

case class MakeAppointDialogElement(e: WebElement):
  def insertName(name: String): Unit =
    e.findElement(ByClassName("name-input")).sendKeys(name)

  def enterButton: WebElement =
    e.findElement(By.xpath("//button[text()='入力']"))
