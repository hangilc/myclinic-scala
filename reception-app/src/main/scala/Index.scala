package dev.myclinic.scala.web.reception

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

@JSExportTopLevel("JsMain")
object JsMain:
  @JSExport
  def main(isAdmin: Boolean): Unit =
    document.body(
      div(id := "content")(
        div(id := "banner")("受付"),
        div(id := "workarea")(
          div(id := "side-bar")(
            div(id := "side-menu")(
              a("メイン"),
              a("患者管理"),
              a("診療記録"),
              a("スキャン"),
            ),
            textarea(id := "hotline-input"),
            div(id := "hotline-commands")(
              button("送信"),
              button("了解"),
              button("Beep"),
              a("常用"),
              a("患者")
            ),
            textarea(id := "hotline-messages")
          ),
          div(id := "main", "MAIN")
        )
      )
    )
