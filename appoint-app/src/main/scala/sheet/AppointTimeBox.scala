package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Binding.{given, *}
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.fujiwara.domq.Bootstrap
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.raw.EventTarget
import org.scalajs.dom.document

case class AppointTimeBox(
      appointTime: AppointTime
  ):
    case class Slot(appoint: Appoint, ele: HTMLElement)
    var slots: List[Slot] = List.empty
    val slotsElement = div()
    val ele =
      div(css(style => style.cursor = "pointer"), onclick := (onElementClick))(
        div(timeLabel),
        slotsElement,
      )

    def init(appoints: List[Appoint]): Unit =
      slots = appoints.map(makeSlot(_))
      slots.foreach(s => slotsElement(s.ele))

    def appoints: List[Appoint] =
      slots.map(slot => slot.appoint)

    def addAppoint(appoint: Appoint): Unit =
      val slot = makeSlot(appoint)
      slots = slots ++ List(slot)
      slotsElement(slot.ele)

    def removeAppoint(appoint: Appoint): Unit =
      slots.find(s => s.appoint == appoint).map(s => {
        slots = slots.filter(_ != s)
        s.ele.remove()
      })

    def makeSlot(appoint: Appoint): Slot =
      Slot(appoint, div(appoint.patientName))

    def timeLabel: String =
      val f = Misc.formatAppointTime(appointTime.fromTime)
      val u = Misc.formatAppointTime(appointTime.untilTime)
      s"$f - $u"

    def onElementClick(event: MouseEvent): Unit =
      if slots.isEmpty && appointTime.capacity > 0 then 
        makeAppointDialog()
      else if slots.size == 1 && appointTime.capacity == 1 then
        cancelAppointDialog(slots.head.appoint)
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
        () => Api.cancelAppoint(appoint.appointId)
      )

object AppointTimeBox:
  def apply(appointTime: AppointTime, appoints: List[Appoint]): AppointTimeBox =
    val box = AppointTimeBox(appointTime)
    box.init(appoints)
    box