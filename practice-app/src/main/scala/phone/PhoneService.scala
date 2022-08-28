package dev.myclinic.scala.web.practiceapp.phone

import dev.myclinic.scala.web.appbase.SideMenuService
import org.scalajs.dom.HTMLElement

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.practice.twilio.Call
import dev.myclinic.scala.web.practiceapp.practice.twilio.TwilioPhone
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.webclient.global

case class PhoneService() extends SideMenuService:
  val phoneNumberInput = input
  override def getElement: HTMLElement =
    div(
      cls := "practice-phone practice-sidemenu-service-main",
      div("電話", cls := "practice-sidemenu-service-title"),
      div(
        "電話番号：",
        phoneNumberInput,
        button("発信", onclick := (doCall _)),
        button(
          "終了",
          onclick := (() => PracticeBus.twilioPhone.hangup())
        )
      )
    )

  def doCall(): Unit =
    TwilioPhone
      .canonicalPhoneNumber(phoneNumberInput.value.trim)
      .foreach(pn =>
        if !PracticeBus.twilioPhone.call(pn) then
          ShowMessage.showError("電話を使用できません。")
      )
