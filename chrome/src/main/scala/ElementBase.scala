package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.By

class ElementBase(e: WebElement):
  def findButtonByText(text: String): WebElement =
    e.findElement(By.xpath(s"//button[text()='${text}']"))

