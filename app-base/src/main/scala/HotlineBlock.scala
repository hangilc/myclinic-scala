package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import io.circe.parser.decode
import dev.myclinic.scala.model.Hotline
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.myclinic.scala.model.AppModelEvent
import scala.util.Success
import scala.util.Failure

class HotlineBlock(sendAs: String, sendTo: String)(using fetcher: EventFetcher):
  val ui = new HotlineBlockUI
  ui.sendButton(onclick := (() => onSend(ui.messageInput.value.trim)))
  ui.rogerButton(onclick := (() => onSend("了解")))
  ui.beepButton(onclick := (() => { Api.hotlineBeep(sendTo); () }))

  def ele = ui.ele
  def init(): Future[Unit] =
    for hotlines <- Api.listTodaysHotline()
    yield
      var lastAppEventId = 0
      hotlines.foreach(event =>
        val hotline = decodeHotline(event.data)
        handleHotline(hotline)
        lastAppEventId = event.appEventId
      )
      fetcher.catchup(
        lastAppEventId,
        event =>
          if isHotlineCreatedEvent(event.model, event.kind) then
            handleHotline(event.data.asInstanceOf[Hotline])
      )
      fetcher.appModelEventPublisher.subscribe(event => {
        if isHotlineCreatedEvent(event.model, event.kind) then
          handleHotline(event.dataAs[Hotline])
      })
      fetcher.hotlineBeepEventPublisher.subscribe(event => {
        if event.recipient == sendAs then
          Api.beep()
      })

  private def decodeHotline(data: String): Hotline =
    decode[Hotline](data).toOption.get
  private def isHotlineCreatedEvent(model: String, kind: String): Boolean =
    model == Hotline.modelSymbol && kind == AppModelEvent.createdSymbol
  private def handleHotline(hotline: Hotline): Unit =
    val recipient = hotline.recipient
    if recipient == sendAs then
      val rep = HotlineEnv.hotlineNameRep(hotline.sender)
      val msg = hotline.message
      val line = s"${rep}> ${msg}\n"
      ui.messages.value += line
  private def onSend(msg: String): Unit =
    if !msg.isEmpty then
      val h = Hotline(msg, sendAs, sendTo)
      Api.postHotline(h).onComplete {
        case Success(_)  => ui.messageInput.value = ""
        case Failure(ex) => ShowMessage.showError(ex.getMessage)
      }

class HotlineBlockUI:
  import dev.fujiwara.domq.PullDownLink
  val messageInput = textarea
  val sendButton = button
  val rogerButton = button
  val beepButton = button
  val regularsLink = PullDownLink("常用")
  val patientsLink = PullDownLink("患者")
  val messages = textarea
  val ele =
    div(cls := "hotline-block")(
      messageInput(cls := "hotline-block-message-input"),
      div(cls := "hotline-block-commands")(
        sendButton("送信"),
        rogerButton("了解"),
        beepButton("Beep"),
        regularsLink.link,
        patientsLink.link
      ),
      messages(
        cls := "hotline-block-messages",
        attr("readonly") := "readonly",
        attr("tabindex") := "-1"
      )
    )
