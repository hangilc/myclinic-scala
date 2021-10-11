package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.ContextMenu
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global

class AdminAppointTimeBox(appointTime: AppointTime)
    extends AppointTimeBox(appointTime):
  ele.addEventListener(
    "contextmenu",
    (event: MouseEvent) => {
      event.preventDefault
      ContextMenu(
        "Convert" -> doConvert,
        "Combine" -> doCombine,
      ).show(event)
    }
  )

  def doConvert(): Unit =
    import dev.fujiwara.domq.ShowMessage
    import ShowMessage.AskCommand.*
    ShowMessage.getString("テスト", "入力してください", println(_))
    

  def doCombine(): Unit =
    for
      appoints <- Api.listAppointTimesForDate(appointTime.date)
    yield ()
