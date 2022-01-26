package dev.myclinic.scala.web.appbase

import org.scalajs.dom.{HTMLTextAreaElement, HTMLElement}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.Hotline
import scala.util.{Success, Failure}

trait HotlineUI:
  def messageInput: HTMLTextAreaElement
  def sendButton: HTMLElement
  def rogerButton: HTMLElement
  def beepButton: HTMLElement

class HotlineHandler(ui: HotlineUI, sendAs: String, sendTo: String):
  ui.sendButton(onclick := (() => onSend(ui.messageInput.value.trim)))
  ui.rogerButton(onclick := (() => onSend("了解")))

  def onSend(msg: String): Unit =
    if !msg.isEmpty then
      val h = Hotline(msg, sendAs, sendTo)
      Api.postHotline(h).onComplete {
        case Success(_)  => ()
        case Failure(ex) => ShowMessage.showError(ex.getMessage)
      }

    