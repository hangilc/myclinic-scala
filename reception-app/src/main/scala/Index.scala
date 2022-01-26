package dev.myclinic.scala.web.reception

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document
import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Html.*
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import dev.myclinic.scala.web.appbase.ElementDispatcher.*

import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.model.{Hotline, AppModelEvent}
import dev.myclinic.scala.web.appbase.{
  EventFetcher,
  EventPublishers
}
import scala.concurrent.Future
import dev.myclinic.scala.model.AppEvent

@JSExportTopLevel("JsMain")
object JsMain:
  val ui = createUI()
  given EventPublishers = ReceptionEventFetcher.publishers

  @JSExport
  def main(isAdmin: Boolean): Unit =
    document.body(ui.ele)
    setupHotline()
    ReceptionEventFetcher.start().onComplete {
      case Success(_)  => ()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }
    ui.invoke("メイン")

  def createUI(): MainUI =
    new MainUI:
      def postHotline(msg: String): Unit =
        if !msg.isEmpty then
          val h = Hotline(msg, Setting.hotlineSender, Setting.hotlineRecipient)
          Api.postHotline(h).onComplete {
            case Success(_)  => ()
            case Failure(ex) => ShowMessage.showError(ex.getMessage)
          }

  def setupHotline(): Unit =
    for _ <- loadHotlines()
    yield {
      setupHotlineSubscriber()
    }

  def loadHotlines(): Future[Unit] =
    for hotlines <- Api.listTodaysHotline()
    yield hotlines.foreach((appEventId, hotlineCreated) =>
      ui.appendHotline(appEventId, hotlineCreated)
    )

  def setupHotlineSubscriber()(using eventPublishers: EventPublishers): Unit =
    val subscriber = eventPublishers.hotlineCreated.subscribe((event, raw) => {
      val appEventId = raw.appEventId
      val hotline = event.created
      if hotline.sender == "reception" || hotline.recipient == "reception" then
        ui.appendHotline(appEventId, event)
    })
    subscriber.start()

object ReceptionEventFetcher extends EventFetcher:
  val publishers = EventPublishers()
  publishers.shahokokuho.addDispatchers()
  publishers.koukikourei.addDispatchers()
  publishers.roujin.addDispatchers()
  override def publish(event: AppModelEvent, raw: AppEvent): Unit =
    import dev.myclinic.scala.model.*
    publishers.publish(event, raw)
