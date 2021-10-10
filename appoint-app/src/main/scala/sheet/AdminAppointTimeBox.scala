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

class AdminAppointTimeBox(appointTime: AppointTime)
    extends AppointTimeBox(appointTime):
  ele.addEventListener(
    "contextmenu",
    (event: MouseEvent) => {
      event.preventDefault
      ContextMenu(
        "Convert" -> (() => println("Convert")),
        "Combine" -> (() => println("Combine")),
      ).show(event)
    }
  )
