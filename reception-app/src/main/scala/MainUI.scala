package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{HotlineCreated}
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

abstract class MainUI:
  def postHotline(msg: String): Unit

  private var lastHotlineAppEventId = 0
  private val hotlineInput = textarea()
  private val hotlineMessages = textarea()
  val ele =
    div(id := "content")(
      div(id := "banner")("受付"),
      div(id := "workarea")(
        div(id := "side-bar")(
          div(id := "side-menu")(
            a("メイン"),
            a("患者管理"),
            a("診療記録"),
            a("スキャン")
          ),
          hotlineInput(id := "hotline-input"),
          div(id := "hotline-commands")(
            button(
              "送信",
              onclick := (() => {
                postHotline(hotlineInput.value)
                hotlineInput.value = ""
              })
            ),
            button(
              "了解",
              onclick := (() => {
                postHotline("了解")
              })
            ),
            button("Beep", onclick := (() => { Api.beep(); ()})),
            a(
              span( "常用", downTriangle()),
              onclick := (doRegular _)
            ),
            a("患者", downTriangle(), onclick := (doPatients _))
          ),
          hotlineMessages(
            id := "hotline-messages",
            attr("readonly") := "readonly",
            attr("tabindex") := "-1"
          )
        ),
        div(id := "main")
      )
    )

  def appendHotline(evt: HotlineCreated): Unit =
    val id = evt.appEventId
    if id > lastHotlineAppEventId then
      val rep = Setting.hotlineNameRep(evt.created.sender)
      val msg = evt.created.message
      hotlineMessages.value += s"${rep}> ${msg}\n"
      lastHotlineAppEventId = id

  private def downTriangle(): HTMLElement =
    Icons.downTriangleFlat(size = "0.6rem", color = Colors.primary)(
      Icons.defaultStaticStyle,
      ml := "0.2rem"
    )

  private def doRegular(event: MouseEvent): Unit =
    val items: List[String] = Setting.regularHotlineMessages
    def cmd(msg: String): Unit = 
      val start = hotlineInput.selectionStart
      val end = hotlineInput.selectionEnd
      val left = hotlineInput.value.substring(0, start)
      val right = hotlineInput.value.substring(end)
      val index = msg.indexOf("{}")
      val msgLeft = if index < 0 then msg else msg.substring(0, index)
      val msgRight = if index < 0 then "" else msg.substring(index + 2)
      hotlineInput.value = left + msgLeft + msgRight + right
      hotlineInput.focus()
      val pos = start + msgLeft.size
      hotlineInput.selectionStart = pos
      hotlineInput.selectionEnd = pos

    val menu = ContextMenu(items.map(
      msg => msg -> (() => cmd(msg))
    ))
    menu.open(event)

private def doPatients(event: MouseEvent): Unit =
  val f = 
    for
      wqueue <- Api.listWqueue()
      visitIds = wqueue.map(_.visitId)
      visitMap <- Api.batchGetVisit(visitIds)
    yield {
      println(("wqueue", wqueue, visitMap))
    }
  f.onComplete {
    case Success(_) => ()
    case Failure(ex) => System.err.println(ex.getMessage)
  }


