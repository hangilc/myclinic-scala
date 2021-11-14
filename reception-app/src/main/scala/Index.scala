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
  @JSExport
  def main(isAdmin: Boolean): Unit =
    ReceptionEventFetcher.start()
    given EventPublishers = ReceptionEventFetcher.publishers
    val ui = createUI()
    document.body(ui.ele)
    
  def createUI(): MainUI =
    new MainUI:
      def postHotline(msg: String): Unit =
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