package dev.myclinic.scala.web

import scalatags.JsDom.all._
import org.scalajs.dom.{document, window}

object Dialog {
  val titleSpan = span("TITLE").render
  val dom = div(cls := "modal-dialog-content", maxHeight := calcMaxHeight())(
    div(cls := "d-flex title-wrapper justify-content-between")(
      h3(cls := "d-inline-block")(titleSpan),
      a(href := "javascript:void(0)", style := "font-size: 1.2rem",
        cls := "align-item-center")(raw("&times;"))
    ),
    div(),
    div(cls := "command-box")
  ).render
  val backDrop = div(cls := "modal-dialog-backdrop").render

  def calcMaxHeight(): String = {
    val h = window.innerHeight - 40
    s"${h}px"
  }

  def open(title: String) = {
    titleSpan.innerText = title
    document.body.appendChild(backDrop)
    document.body.appendChild(dom)
  }
}