package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{Appoint, AppointTime}
import dev.fujiwara.domq.Icons
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.ContextMenu
import scala.language.implicitConversions
import org.scalajs.dom.raw.MouseEvent

class AdminAppointSheet extends AppointSheet:
  val cog = Icons.cog(color = "gray")
  TopMenu.ele(
    cog(css(style => style.cssFloat = "right"), attr("id") := "cog")
  )
  cog(onclick := onCogClick)

  override def makeAppointTimeBox(
      appointTime: AppointTime,
  ): AppointTimeBox =
    AdminAppointTimeBox(appointTime)

  def onCogClick(event: MouseEvent): Unit =
    ContextMenu("予約枠わりあて" -> doFillAppointTimes).show(event)

  def doFillAppointTimes(): Unit =
    println("Fill")
    
