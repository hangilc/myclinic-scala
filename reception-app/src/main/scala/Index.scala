package dev.myclinic.scala.web.reception

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.model.{Hotline, AppModelEvent}
import dev.myclinic.scala.web.appbase.{
  EventFetcher,
  EventDispatcher,
  EventPublishers
}

@JSExportTopLevel("JsMain")
object JsMain:
  val hotlineInput = textarea()
  @JSExport
  def main(isAdmin: Boolean): Unit =
    ReceptionEventFetcher.start()
    given EventPublishers = ReceptionEventFetcher.publishers
    document.body(
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
              button("送信", onclick := (postHotline _)),
              button("了解"),
              button("Beep"),
              a("常用"),
              a("患者")
            ),
            textarea(
              id := "hotline-messages",
              attr("readonly") := "readonly",
              attr("tabindex") := "-1"
            )
          ),
          div(id := "main")
        )
      )
    )

  def postHotline(): Unit =
    val msg = hotlineInput.value
    if !msg.isEmpty then
      val h = Hotline(msg, Setting.hotlineSender, Setting.hotlineRecipient)
      Api.postHotline(h).onComplete {
        case Success(_)  => ()
        case Failure(ex) => ShowMessage.showError(ex.getMessage)
      }

object ReceptionEventFetcher extends EventFetcher:
  val publishers = EventDispatcher()
  override def publish(event: AppModelEvent): Unit =
    publishers.publish(event)