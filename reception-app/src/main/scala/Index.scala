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
    // document.body(ui.ele)
    // setupHotline()
    // ReceptionEventFetcher.start().onComplete {
    //   case Success(_)  => ()
    //   case Failure(ex) => ShowMessage.showError(ex.getMessage)
    // }
//    ui.invoke("メイン")
    {
      import dev.fujiwara.domq.{FloatingElement, Geometry, PullDown, Icons}
      import dev.fujiwara.domq.Geometry.*
      import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
      val btn: HTMLElement = PullDown("患者設定", div("CONTENT")).ele
      document.body(
        div(
          btn,
          Icons.calendar,
          Icons.cog,
          Icons.check,
          Icons.checkCircle,
          Icons.circle,
          Icons.circleFilled,
          Icons.dotsHorizontal,
          Icons.dotsVertical,
          Icons.downTriangle,
          Icons.downTriangleFlat,
          Icons.menu,
          Icons.menuAlt4,
          Icons.pencil,
          Icons.pencilAlt,
          Icons.plusCircle,
          Icons.refresh,
          Icons.search,
          Icons.trash,
          Icons.x,
          Icons.xCircle,
          Icons.zoomIn,
          Icons.zoomOut,
        )
      )
    }

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
    import dev.myclinic.scala.model.*
    publishers.publish(event)
    event match {
      case e: ShahokokuhoCreated =>
        dispatch(".shahokokuho-created", "shahokokuho-created", e)
      case e: ShahokokuhoUpdated =>
        dispatch(".shahokokuho-updated", "shahokokuho-updated", e)
        dispatch(
          s".shahokokuho-${e.updated.shahokokuhoId}",
          "shahokokuho-updated",
          e
        )
      case e: ShahokokuhoDeleted =>
        dispatch(".shahokokuho-deleted", "shahokokuho-deleted", e)
        dispatch(
          s".shahokokuho-${e.deleted.shahokokuhoId}",
          "shahokokuho-deleted",
          e
        )
      case e: KoukikoureiCreated =>
        dispatch(".koukikourei-created", "koukikourei-created", e)
      case e: KoukikoureiUpdated =>
        dispatch(".koukikourei-updated", "koukikourei-updated", e)
        dispatch(
          s".koukikourei-${e.updated.koukikoureiId}",
          "koukikourei-updated",
          e
        )
      case e: KoukikoureiDeleted =>
        dispatch(".koukikourei-deleted", "koukikourei-deleted", e)
        dispatch(
          s".koukikourei-${e.deleted.koukikoureiId}",
          "koukikourei-deleted",
          e
        )
      case e: KouhiCreated => dispatch(".kouhi-created", "kouhi-created", e)
      case e: KouhiUpdated =>
        dispatch(".kouhi-updated", "kouhi-updated", e)
        dispatch(s".kouhi-${e.updated.kouhiId}", "kouhi-updated", e)
      case e: KouhiDeleted =>
        dispatch(".kouhi-deleted", "kouhi-deleted", e)
        dispatch(s".kouhi-${e.deleted.kouhiId}", "kouhi-deleted", e)
      case _ => ()
    }

  def dispatch[T](selector: String, eventType: String, detail: T): Unit =
    import dev.fujiwara.domq.{CustomEvent, CustomEventInit}
    val evt: CustomEvent[T] = CustomEvent(eventType, detail, false)
    document.body
      .qSelectorAll(selector)
      .foreach(e => {
        e.dispatchEvent(evt)
      })
