package dev.fujiwara.domq

import org.scalajs.dom.{Element, HTMLElement}
import org.scalajs.dom.{document, window}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import scala.language.implicitConversions

class Modal(title: String, content: HTMLElement, val zIndex: Int = Modal.zIndexDefault):
  val backdrop = div(Modal.modalBackdrop(zIndex - 1))
  val auxMenu: HTMLElement = span()
  val closeIcon = Icons.x
  val workarea = content
  var onCloseCallbacks: List[Boolean => Unit] = List.empty
  val dialog = div(Modal.modalContent(zIndex), cls := "domq-modal")(
    div(
      css(style => style.width = "*"),
      span(Modal.modalTitle)(title),
      span(css(style => {
        style.marginLeft = "2rem"
        style.cssFloat = "right"
      }))(
        auxMenu,
        closeIcon(
          Icons.defaultStyle,
          onclick := (close _)
        )
      )
    ),
    content
  )
  def open(): Unit =
    document.body(backdrop, dialog)

  def close(value: Boolean): Unit =
    dialog.remove()
    backdrop.remove()
    onCloseCallbacks.foreach(_(value))

  def close(): Unit = close(false)

  def onClose(cb: Boolean => Unit): Unit =
    onCloseCallbacks = onCloseCallbacks :+ cb

class ModalModifiers:
  def modalBackdrop(zIndex: Int) = Modifier[HTMLElement](e => {
    val style = e.style
    style.display = "block"
    style.position = "fixed"
    style.left = "0"
    style.top = "0"
    style.right = "0"
    style.bottom = "0"
    style.backgroundColor = "#5a6268"
    style.opacity = "0.4"
    style.overflowY = "auto"
    style.zIndex = zIndex.toString
  })

  val modalTitle = Modifier[HTMLElement](e => {
    val style = e.style
    style.verticalAlign = "middle"
    style.fontSize = "1.2rem"
    style.fontWeight = "bold"
  })

  def modalContent(zIndex: Int) = Modifier[HTMLElement](e => {
    val style = e.style
    style.position = "fixed"
    style.top = "20px"
    style.left = "50vw"
    style.transform = "translateX(-50%)"
    style.backgroundColor = "white"
    style.padding = "0.5rem 1.5rem"
    style.opacity = "1.0"
    style.zIndex = zIndex.toString
    style.overflow = "auto"
    style.borderRadius = "0.5rem"
    style.maxHeight = (window.innerHeight - 60).toString + "px"
  })

  val modalBody = Modifier[HTMLElement](e => {
    val style = e.style
    style.padding = "0.5em 0"
  })

  val modalCommands = Modifier[HTMLElement](e => {
    val style = e.style
    e.style.textAlign = "end"
    e(cls := "modal-commands")
  })

object Modal extends ModalModifiers:
  def enter = button("入力")
  def cancel = button("キャンセル")
  def ok = button("ＯＫ")
  def execute = button("実行")
  def yes = button("はい")
  val zIndexDefault = 2002

  def apply(title: String, content: HTMLElement): Modal =
    new Modal(title, content(modalBody))

  def apply[T](
      title: String,
      body: HTMLElement,
      commands: HTMLElement
  ): Modal =
    new Modal(
      title,
      div(
        body(modalBody),
        commands(modalCommands)
      )
    )
