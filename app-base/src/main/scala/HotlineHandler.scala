package dev.myclinic.scala.web.appbase

import org.scalajs.dom.{HTMLTextAreaElement, HTMLElement}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.{Hotline, HotlineCreated}
import scala.concurrent.Future
import scala.util.{Success, Failure}

trait HotlineUI:
  def messageInput: HTMLTextAreaElement
  def sendButton: HTMLElement
  def rogerButton: HTMLElement
  def beepButton: HTMLElement

class HotlineHandler(
    ui: HotlineUI,
    sendAs: String,
    sendTo: String,
    fetcher: EventFetcher,
    publishers: EventPublishers
):
  var lastAppEventId = 0

  def init(): Future[Unit] =
    for
      hotlines <- Api.listTodaysHotline()
      _ = hotlines.foreach(pair => pair match {
        case (appEventId, event) => handleHotline(appEventId, event.created)
      })
    yield
      fetcher.catchup(lastAppEventId, (gen, event) => event match {
        case HotlineCreated(_, created) => handleHotline(gen, created)
        case _ => ()
      })
      publishers.hotlineCreated.subscribe((appEventId, event) => {
        handleHotline(appEventId, event.created)
      })
      ui.sendButton(onclick := (() => onSend(ui.messageInput.value.trim)))
      ui.rogerButton(onclick := (() => onSend("了解")))
      ui.beepButton(onclick := (() => { Api.hotlineBeep(sendTo); () }))

  def handleHotline(appEventId: Int, hotline: Hotline): Unit =
    lastAppEventId = appEventId

  def onSend(msg: String): Unit =
    if !msg.isEmpty then
      val h = Hotline(msg, sendAs, sendTo)
      Api.postHotline(h).onComplete {
        case Success(_)  => ui.messageInput.value = ""
        case Failure(ex) => ShowMessage.showError(ex.getMessage)
      }
