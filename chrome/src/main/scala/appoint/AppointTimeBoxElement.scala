package dev.myclinic.scala.chrome.appoint

import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import scala.jdk.CollectionConverters.*

case class AppointTimeBoxElement(e: WebElement):
  def dataTime: String =
    e.getAttribute("data-time")

  def slots: List[AppointSlotElement] =
    e.findElements(ByClassName("appoint-slot"))
      .asScala
      .toList
      .map(AppointSlotElement.apply)
