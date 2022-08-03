package dev.myclinic.scala.chrome

import org.openqa.selenium.WebElement
import org.openqa.selenium.By

object ElementUtil:
  def getButtonByText(wrapper: WebElement, text: String): WebElement =
    wrapper.findElement(By.xpath(s"//button[text()='${text}']"))

