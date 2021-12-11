package dev.fujiwara.domq

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import scala.scalajs.js

case class FloatWindow(
    title: String,
    content: HTMLElement,
    zIndex: Option[Int] = None,
    width: String = "200px"
):
  val eTitle: HTMLElement = div()
  val ele = div(css(style => {
    style.width = width
    style.position = "absolute"
    style.border = "1px solid gray"
    style.padding = "4px"
    style.backgroundColor = "white"
    style.borderRadius = "4px"
    style.zIndex = zIndex.getOrElse(1980).toString
  }))(
    eTitle(css(style => {
      style.fontWeight = "bold"
      style.backgroundColor = "#eee"
      style.padding = "2px"
      style.marginBottom = "4px"
      style.cursor = "grab"
      style.setProperty("user-select", "none")
    }))(
      div(css(style => {
        style.display = "flex"
        style.setProperty("align-items", "center")
        style.setProperty("justify-content", "space-between")
      }))(
        title,
        Icons.x(
          css(style => style.cssFloat = "right"),
          Icons.defaultStyle
        )(
          onclick := (close _)
        )
      )
    )(
      onmousedown := (onMouseDown _),
      onmouseup := (onMouseUp _)
    ),
    content
  )
  var prevClickX: Double = 0
  var prevClickY: Double = 0

  def open(): Unit =
    val body: HTMLElement = document.body
    body(ele)
    Position.placeAtWindowCenter(ele)

  def close(): Unit =
    ele.remove()

  val onMouseMove: js.Function1[MouseEvent, Unit] = (event: MouseEvent) => {
    val x = event.clientX
    val y = event.clientY
    val dx = x - prevClickX
    val dy = y - prevClickY
    ele(css(style => {
      val rect = ele.getClientRects()(0)
      style.left = s"${rect.left + dx + window.scrollX}px"
      style.top = s"${rect.top + dy + window.scrollY}px"
    }))
    prevClickX = x
    prevClickY = y
  }

  val onMouseLeave: js.Function1[MouseEvent, Unit] = (event: MouseEvent) => {
    eTitle(onmousemove :- onMouseMove)
    eTitle(onmouseleave :- onMouseLeave)
  }

  def onMouseDown(event: MouseEvent): Unit =
    prevClickX = event.clientX
    prevClickY = event.clientY
    eTitle(onmousemove := onMouseMove)
    eTitle(onmouseleave := onMouseLeave)

  def onMouseUp(event: MouseEvent): Unit =
    eTitle(onmousemove :- onMouseMove)
    eTitle(onmouseleave :- onMouseLeave)
