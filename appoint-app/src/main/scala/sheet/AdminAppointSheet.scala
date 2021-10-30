package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{Appoint, AppointTime}
import dev.fujiwara.domq.Icons
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.ContextMenu
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api

class AdminAppointSheet extends AppointSheet:
  val cog = Icons.cog(color = "gray")(Icons.defaultStyle)
  TopMenu.topMenuBox(cog)
  cog(onclick := onCogClick)

  override def makeAppointTimeBox(
      appointTime: AppointTime,
  ): AppointTimeBox =
    AdminAppointTimeBox(appointTime)

  def onCogClick(event: MouseEvent): Unit =
    ContextMenu("予約枠わりあて" -> doFillAppointTimes).open(event)

  def doFillAppointTimes(): Unit =
    dateRange.map {
      case (from, upto) => Api.fillAppointTimes(from, upto)
    }
    
