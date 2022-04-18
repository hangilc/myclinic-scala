package dev.myclinic.scala.web.practiceapp.practice

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.*

case class RightWidget(title: String, content: HTMLElement):
  val ele = div(cls := "practice-right-widget",
    div(cls := "practice-right-widget-title", title),
    div(cls := "practice-right-widget-content", content)
  )

