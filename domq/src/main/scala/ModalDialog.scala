package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, document, window}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

class ModalDialog:
  private val zIndexScreen = ZIndexManager.alloc()
  private val zIndexContent = ZIndexManager.alloc()
  private val screen: HTMLElement = div(cls := "domq-modal-dialog-screen", zIndex := zIndexScreen)
  val content: HTMLElement = div(cls := "domq-modal-dialog-content", zIndex := zIndexContent)
  content(maxHeight := (window.innerHeight - 60).toString + "px")

  def open(): Unit =
    document.body(screen, content)

  def close(): Unit =
    content.remove()
    screen.remove()
    ZIndexManager.release(zIndexContent)
    ZIndexManager.release(zIndexScreen)

class ModalDialog3 extends ModalDialog:
  val title: HTMLElement = div(cls := "domq-modal-dialog3-title")
  val body: HTMLElement = div(cls := "domq-modal-dialog3-body")
  val commands: HTMLElement = div(cls := "domq-modal-dialog3-commands")
  content(title, body, commands)

