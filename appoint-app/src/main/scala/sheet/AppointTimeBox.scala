package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.model.{AppointTime, Appoint, given}
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
import dev.myclinic.scala.util.DateTimeOrdering.given
import scala.math.Ordered.orderingToOrdered
import java.time.LocalDate
import dev.myclinic.scala.web.appoint.sheet.Types.SortedElement

given Ordering[AppointTimeBox] with
  def compare(a: AppointTimeBox, b: AppointTimeBox): Int =
    summon[Ordering[AppointTime]].compare(a.appointTime, b.appointTime)

val sortedAppointTimeBox: SortedElement[AppointTimeBox] =
  new SortedElement[AppointTimeBox]:
    def element(a: AppointTimeBox): HTMLElement = a.ele

case class AppointTimeBox(
    appointTime: AppointTime
):
  case class Slot(appoint: Appoint, ele: HTMLElement)
  var slots: List[Slot] = List.empty
  val slotsElement = div()
  val ele =
    div(css(style => style.cursor = "pointer"), onclick := (onElementClick))(
      div(timeLabel),
      slotsElement
    )

  def appointTimeId: Int = appointTime.appointTimeId

  def init(appoints: List[Appoint]): Unit =
    slots = appoints.map(makeSlot(_))
    slots.foreach(s => slotsElement(s.ele))

  def appoints: List[Appoint] =
    slots.map(slot => slot.appoint)

  def addAppoint(appoint: Appoint): Unit =
    val slot = makeSlot(appoint)
    slots = slots ++ List(slot)
    slotsElement(slot.ele)

  def addAppoints(appoints: Seq[Appoint]): Unit =
    appoints.foreach(addAppoint(_))

  def removeAppoint(appoint: Appoint): Unit =
    slots
      .find(s => s.appoint == appoint)
      .map(s => {
        slots = slots.filter(_ != s)
        s.ele.remove()
      })

  def makeSlot(appoint: Appoint): Slot =
    val name: String = s"${appoint.patientName}"
    val memo: String = if appoint.memo.isEmpty then "" else s" （${appoint.memo}）"
    val rep = name + memo
    Slot(appoint, div(rep))

  def timeLabel: String =
    val f = Misc.formatAppointTime(appointTime.fromTime)
    val u = Misc.formatAppointTime(appointTime.untilTime)
    s"$f - $u"

  def onElementClick(event: MouseEvent): Unit =
    if slots.isEmpty && appointTime.capacity > 0 then makeAppointDialog()
    else if slots.size == 1 && appointTime.capacity == 1 then
      cancelAppointDialog(slots.head.appoint)
    else ()

  def makeAppointDialog(): Unit =
    MakeAppointDialog.open(appointTime)

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
