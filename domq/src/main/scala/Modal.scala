package dev.fujiwara.domq

import org.scalajs.dom.raw.{Element, HTMLElement}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import scala.language.implicitConversions

class Modal(title: String, content: HTMLElement):
  val closeIcon = Icons.x(color = "gray")
  val workarea = content
  var onCloseCallbacks: List[() => Unit] = List.empty
  val dialog = div(Modal.modalContent)(
    div(
      css(style => style.width = "*"),
      span(Modal.modalTitle)(title),
      closeIcon(
        css(style => {
          style.cssFloat = "right"
          style.verticalAlign = "middle"
          style.marginLeft = "2rem"
          style.cursor = "pointer"
        })
      )(onclick := (close _))
    ),
    content
  )
  def open(): Unit =
    document.body(Modal.modalBackdropInstance, dialog)

  def close(): Unit =
    dialog.remove()
    Modal.modalBackdropInstance.remove()
    onCloseCallbacks.foreach(cb => cb())

  def onClose(cb: () => Unit): Unit =
    onCloseCallbacks = onCloseCallbacks :+ cb

class ModalModifiers:
  val modalBackdrop = Modifier(e => {
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
    style.zIndex = "2001"
  })

  val modalBackdropInstance: HTMLElement = div(modalBackdrop)

  val modalTitle = Modifier(e => {
    val style = e.style
    style.verticalAlign = "middle"
    style.fontSize = "1.2rem"
    style.fontWeight = "bold"
  })

  val modalContent = Modifier(e => {
    val style = e.style
    style.position = "fixed"
    style.top = "20px"
    style.left = "50vw"
    style.transform = "translateX(-50%)"
    style.backgroundColor = "white"
    style.padding = "0.5rem 1.5rem"
    style.opacity = "1.0"
    style.zIndex = "2002"
    style.overflow = "auto"
    style.borderRadius = "0.5rem"
  })

  val modalBody = Modifier(e => {
    val style = e.style
    style.padding = "0.5em 0"
  })

  val modalCommands = Modifier(e => {
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
