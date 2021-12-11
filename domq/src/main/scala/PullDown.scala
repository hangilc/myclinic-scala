package dev.fujiwara.domq

import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Geometry, Icons}
import scala.language.implicitConversions

class PullDown(label: String, content: HTMLElement):
  val ele: HTMLElement = button(cls := "domq-pull-down-button")(
    "患者選択",
    Icons.downTriangleFlat()(cls := "domq-pull-down-arrow")
    // Icons.downTriangleFlat(size = "0.7rem", color = "#989898")(
    //   ml := "0.2rem",
    //   css(style => {
    //     style.position = "relative"
    //     style.top = "0.08rem"
    //   })
    // )
  )
