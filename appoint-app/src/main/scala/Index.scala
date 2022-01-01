package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modifiers._
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import org.scalajs.dom
import org.scalajs.dom.document
import java.time.LocalDate
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.webclient.{Api, UserError}
import scala.language.implicitConversions
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.web.appoint.sheet.{AppointSheet, AdminAppointSheet}
//import dev.myclinic.scala.event.ModelEventPublishers
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.{
  EventFetcher,
  EventPublishers,
  EventDispatcher
}

@JSExportTopLevel("JsMain")
object JsMain:
  @JSExport
  def main(isAdmin: Boolean): Unit =
    val body = document.body
    val content = div(attr("id") := "content")
    val workarea = div()
    body(
      content(
        banner(isAdmin),
        workarea
      )
    )
    AppEvents.start()
    given EventPublishers = AppEvents.publishers
    val sheet = if isAdmin then AdminAppointSheet() else AppointSheet()
    val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    val endDate = startDate.plusDays(6)
    sheet.setupTo(workarea)
    sheet.setupDateRange(startDate, endDate).onComplete {
      case Success(_) => ()
      case Failure(e) => System.err.println(e)
    }

  def banner(isAdmin: Boolean): HTMLElement =
    val text = "診察予約" + (if isAdmin then "（管理）" else "")
    div(text)(cls := "banner")

object AppEvents extends EventFetcher:
  val publishers = EventDispatcher()
  override def publish(event: AppModelEvent): Unit =
    publishers.publish(event)
