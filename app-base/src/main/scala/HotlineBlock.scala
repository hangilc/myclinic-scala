package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}

class HotlineBlock:
  val ui = new HotlineBlockUI

  def ele = ui.ele

class HotlineBlockUI:
  import dev.fujiwara.domq.PullDownLink
  val messageInput = textarea
  val sendButton = button
  val rogerButton = button
  val beepButton = button
  val regularsLink = PullDownLink("常用")
  val patientsLink = PullDownLink("患者")
  val messages = textarea
  val ele =
    div(cls := "hotline-block")(
      messageInput(cls := "hotline-block-message-input"),
      div(cls := "hotline-block-commands")(
        sendButton("送信"),
        rogerButton("了解"),
        beepButton("Beep"),
        regularsLink.link,
        patientsLink.link
      ),
      messages(
        cls := "hotline-block-messages",
        attr("readonly") := "readonly",
        attr("tabindex") := "-1"
      )
    )
