package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

class ModalDialog(stuffer: HTMLElement => Unit):
  private val zIndexScreen = ZIndexManager.alloc()
  private val zIndexContent = ZIndexManager.alloc()
  private val screen: HTMLElement = div(cls := "domq-modal-dialog-screen", zIndex := zIndexScreen)
  private val conent: HTMLElement = div(cls := "domq-modal-dialog-content", zIndex := zIndexContent )