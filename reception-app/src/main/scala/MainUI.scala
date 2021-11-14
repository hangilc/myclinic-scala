package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions

class MainUI:
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
            button("送信", onclick := (postHotline _)),
            button("了解"),
            button("Beep"),
            a("常用"),
            a("患者")
          ),
          textarea(
            id := "hotline-messages",
            attr("readonly") := "readonly",
            attr("tabindex") := "-1"
          )
        ),
        div(id := "main")
      )
    )
