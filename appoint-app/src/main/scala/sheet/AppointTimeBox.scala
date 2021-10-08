package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import scala.language.implicitConversions
import org.scalajs.dom.raw.Element
import dev.myclinic.scala.webclient.Api

case class AppointTimeBox(
      appointTime: AppointTime,
      var appoints: List[Appoint]
  ):
    val slotsElement = div()
    val ele =
      div(style := "cursor: pointer", onclick := (onElementClick _))(
        div(timeLabel),
        slotsElement,
      )

    appoints.foreach(app => {
      slotsElement(makeSlot(app))
    })

    def addAppoint(appoint: Appoint): Unit =
      appoints = appoints ++ List(appoint)
      slotsElement(makeSlot(appoint))

    def makeSlot(appoint: Appoint): Element =
      div(appoint.patientName)

    def timeLabel: String =
      val f = Misc.formatAppointTime(appointTime.fromTime)
      val u = Misc.formatAppointTime(appointTime.untilTime)
      s"$f - $u"

    def onElementClick(): Unit =
      if appoints.isEmpty && appointTime.capacity > 0 then 
        makeAppointDialog()
      else if appoints.size == 1 && appointTime.capacity == 1 then
        cancelAppointDialog(appoints.head)
      else
        ()
    
    def makeAppointDialog(): Unit =
      MakeAppointDialog.open(
        appointTime,
        name => {
          val app = Appoint(0, 0, appointTime.appointTimeId, name, 0, "")
          Api.registerAppoint(app)
        }
      )
    
    def cancelAppointDialog(appoint: Appoint): Unit =
      CancelAppointDialog.open(
        appointTime,
        appoint,
        dialog => {
          //
          dialog.close()
        }
      )

