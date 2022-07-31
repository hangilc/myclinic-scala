package dev.myclinic.scala.chrome.appoint

import org.openqa.selenium.WebElement
import java.time.LocalDate

case class AppointColumnElement(e: WebElement):
  def date: LocalDate =
    LocalDate.parse(e.getAttribute("data-date"))

