package dev.myclinic.scala.web.appbase

import org.scalajs.dom.{HTMLTextAreaElement, HTMLElement}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.{Hotline, AppModelEvent}
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import scala.concurrent.Future
import scala.util.{Success, Failure}
import io.circe.parser.decode

trait HotlineUI:
  def messageInput: HTMLTextAreaElement
  def sendButton: HTMLElement
  def rogerButton: HTMLElement
  def beepButton: HTMLElement

class HotlineHandler(
    ui: HotlineUI,
    sendAs: String,
    sendTo: String
)(using fetcher: EventFetcher):
  var lastAppEventId = 0

  def decodeHotline(data: String): Hotline =
    decode[Hotline](data).toOption.get

  def init(): Future[Unit] =
    for
      hotlines <- Api.listTodaysHotline()
    yield
      hotlines.foreach(event => 
        val appEventId = event.appEventId
        val hotline = decodeHotline(event.data)
        handleHotline(appEventId, hotline)
      )
      fetcher.catchup(lastAppEventId, event =>
        (event.model, event.kind) match {
          case (Hotline.modelSymbol, AppModelEvent.createdSymbol) =>
            handleHotline(event.appEventId, event.data.asInstanceOf[Hotline])
          case _ => ()
        }
      )
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
