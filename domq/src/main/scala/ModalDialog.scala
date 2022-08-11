package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement, document, window}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

class ModalDialog:
  private var zIndexScreen: Option[Int] = None
  private var zIndexContent: Option[Int] = None
  private val screen: HTMLElement = div(cls := "domq-modal-dialog-screen")
  val content: HTMLElement = div(cls := "domq-modal-dialog-content")
  content(maxHeight := (window.innerHeight - 60).toString + "px")

  def open(className: Option[String] = None): Unit =
    if !zIndexScreen.isDefined then
      zIndexScreen = Some(ZIndexManager.alloc())
      zIndexContent = Some(ZIndexManager.alloc())
      screen(zIndex := zIndexScreen)
      content(zIndex := zIndexContent, cls := className)
      document.body(screen, content)

  def close(): Unit =
    if zIndexScreen.isDefined then
      content(zIndex := None).remove()
      screen(zIndex := None).remove()
      zIndexContent.foreach(zIndex => 
        ZIndexManager.release(zIndex)
        zIndexContent = None
      )
      zIndexScreen.foreach(zIndex =>
        ZIndexManager.release(zIndex)
        zIndexScreen = None
      )

class ModalDialog3 extends ModalDialog:
  val title: HTMLElement = div(cls := "domq-modal-dialog3-title")
  val body: HTMLElement = div(cls := "domq-modal-dialog3-body")
  val commands: HTMLElement = div(cls := "domq-modal-dialog3-commands")
  content(title, body, commands)


