package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{HotlineCreated}

abstract class MainUI:
  def postHotline(msg: String): Unit

  private var lastHotlineAppEventId = 0
  private val hotlineInput = textarea()
  private val hotlineMessages = textarea()
  val ele =
    div(id := "content")(
      div(id := "banner")("受付"),
      div(id := "workarea")(
        div(id := "side-bar")(
          div(id := "side-menu")(
            a("メイン"),
            a("患者管理"),
            a("診療記録"),
            a("スキャン")
          ),
          hotlineInput(id := "hotline-input"),
          div(id := "hotline-commands")(
            button("送信", onclick := (() => postHotline(hotlineInput.value))),
            button("了解"),
            button("Beep"),
            a("常用"),
            a("患者")
          ),
          hotlineMessages(
            id := "hotline-messages",
            attr("readonly") := "readonly",
            attr("tabindex") := "-1"
          )
        ),
        div(id := "main")
      )
    )

  def appendHotline(evt: HotlineCreated): Unit =
    val id = evt.appEventId
    if id > lastHotlineAppEventId then
      val rep = Setting.hotlineNameRep(evt.created.sender)
      val msg = evt.created.message
      hotlineMessages.value += s"${rep}> ${msg}\n"
      lastHotlineAppEventId = id


