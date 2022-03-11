package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.ElementQ.* 
import dev.fujiwara.domq.Html.*
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import org.scalajs.dom
import org.scalajs.dom.document
import java.time.LocalDate
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

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
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.{
  EventFetcher,
  EventPublishers
}

@JSExportTopLevel("JsMain")
object JsMain:
  @JSExport
  def main(isAdmin: Boolean): Unit =
    import AppointEvent.{*, given}
    val body = document.body
    val content = div(attr("id") := "content")
    val workarea = div()
    body(
      content(
        banner(isAdmin),
        workarea
      )
    )
    fetcher.start().onComplete {
      case Failure(ex) => System.err.println(ex.getMessage)
      case Success(_) =>
        val sheet = if isAdmin then AdminAppointSheet() else AppointSheet()
        val startDate = DateUtil.startDayOfWeek(LocalDate.now())
        val endDate = startDate.plusDays(6)
        workarea(clear, sheet.ele)
    }

  def banner(isAdmin: Boolean): HTMLElement =
    val text = "診察予約" + (if isAdmin then "（管理）" else "")
    div(text)(cls := "banner")

object AppointEvent:
  val publishers = new EventPublishers
  given fetcher: EventFetcher = new EventFetcher
  fetcher.appModelEventPublisher.subscribe(event => publishers.publish(event))
  fetcher.hotlineBeepEventPublisher.subscribe(event => publishers.publish(event))