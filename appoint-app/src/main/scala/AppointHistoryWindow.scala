package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.FloatWindow
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{AppEvent, AppModelEvent}
import dev.myclinic.scala.web.appoint.history.History
import org.scalajs.dom.raw.HTMLElement
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AppointHistoryWindow:
  def open(events: List[AppEvent], zIndex: Option[Int] = None): Future[Unit] =
    for histories <- History.fromAppEvents(events)
    yield
      val content: HTMLElement = div(cls := "appoint-history")(
        css(style => {
          style.maxHeight = "360px"
          style.overflowY = "auto"
        }),
        innerText := histories.map(_.description).mkString("\n")
      )
      FloatWindow("変更履歴", content, width = "", zIndex = zIndex).open()
