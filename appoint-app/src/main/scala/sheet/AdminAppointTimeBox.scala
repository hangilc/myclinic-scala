package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions

class AdminAppointTimeBox(appointTime: AppointTime)
    extends AppointTimeBox(appointTime):
  ele.addEventListener("contextmenu", (event: MouseEvent) => {
    event.preventDefault
    println("contextmenu")
  })
