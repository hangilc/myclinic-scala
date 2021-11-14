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
import scala.concurrent.Future

@JSExportTopLevel("JsMain")
object JsMain:
  val ui = createUI()
  given EventPublishers = ReceptionEventFetcher.publishers
  
  @JSExport
  def main(isAdmin: Boolean): Unit =
    document.body(ui.ele)
    setupHotline()
    ReceptionEventFetcher.start()
    
  def createUI(): MainUI =
    new MainUI:
      def postHotline(msg: String): Unit =
        if !msg.isEmpty then
          val h = Hotline(msg, Setting.hotlineSender, Setting.hotlineRecipient)
          Api.postHotline(h).onComplete {
            case Success(_)  => ()
            case Failure(ex) => ShowMessage.showError(ex.getMessage)
          }
      def beep(): Unit = Api.beep()

  def setupHotline(): Unit =
    for
      _ <- loadHotlines()
    yield {
      setupHotlineSubscriber()
    }

  def loadHotlines(): Future[Unit] =
    for
      hotlines <- Api.listTodaysHotline()
    yield hotlines.foreach(ui.appendHotline(_))

  def setupHotlineSubscriber()(using eventPublishers: EventPublishers): Unit =
    val subscriber = eventPublishers.hotlineCreated.subscribe(event => {
      val hotline = event.created
      if hotline.sender == "reception" || hotline.recipient == "reception" then
        ui.appendHotline(event)
    })
    subscriber.start()

object ReceptionEventFetcher extends EventFetcher:
  val publishers = EventDispatcher()
  override def publish(event: AppModelEvent): Unit =
    publishers.publish(event)