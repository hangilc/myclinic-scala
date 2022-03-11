package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import io.circe.parser.decode
import dev.myclinic.scala.model.Hotline
import dev.myclinic.scala.model.jsoncodec.Implicits.given

class HotlineBlock:
  val ui = new HotlineBlockUI

  def ele = ui.ele
  def init(): Future[Unit] =
    for
      hotlines <- Api.listTodaysHotline()
    yield
      hotlines.foreach(event => 
        val appEventId = event.appEventId
        val hotline = event.data
        handleHotline(appEventId, hotline)
      )
      // fetcher.catchup(lastAppEventId, event =>
      //   (event.model, event.kind) match {
      //     case (Hotline.modelSymbol, AppModelEvent.createdSymbol) =>
      //       handleHotline(event.appEventId, event.data.asInstanceOf[Hotline])
      //     case _ => ()
      //   }
      // )
      // ui.sendButton(onclick := (() => onSend(ui.messageInput.value.trim)))
      // ui.rogerButton(onclick := (() => onSend("了解")))
      // ui.beepButton(onclick := (() => { Api.hotlineBeep(sendTo); () }))
  private def decodeHotline(data: String): Hotline =
    decode[Hotline](data).toOption.get
  private def handleHotline(gen: Int, message: String): Unit =
    println(("hotline", message))

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
