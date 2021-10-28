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
import dev.myclinic.scala.web.appoint.sheet.editappoint.EditAppointDialog

given Ordering[AppointTimeBox] with
  def compare(a: AppointTimeBox, b: AppointTimeBox): Int =
    summon[Ordering[AppointTime]].compare(a.appointTime, b.appointTime)

val sortedAppointTimeBox: SortedElement[AppointTimeBox] =
  new SortedElement[AppointTimeBox]:
    def element(a: AppointTimeBox): HTMLElement = a.ele

case class AppointTimeBox(
    appointTime: AppointTime
):
  case class Slot(var appoint: Appoint):
    val ele = div()
    var dialog: Option[EditAppointDialog] = None
    updateUI()

    def updateUI(): Unit =
      ele.innerHTML = ""
      ele(label)(onclick := (onClick _))
    def label: String =
      val patientId: String =
        if appoint.patientId == 0 then ""
        else s"(${appoint.patientId}) "
      val name: String = s"${appoint.patientName}"
      val memo: String =
        if appoint.memo.isEmpty then "" else s" （${appoint.memo}）"
      patientId + name + memo
    def onClick(): Unit =
      val m = EditAppointDialog(appoint, appointTime)
      m.onClose(() => { dialog = None })
      dialog = Some(m)
      m.open()
    def onAppointUpdated(updated: Appoint): Unit =
      appoint = updated
      updateUI()
      dialog.foreach(d => d.onAppointUpdated(updated))

  var slots: List[Slot] = List.empty
  val slotsElement = div()
  val ele =
    div(
      cls := "appoint-time-box",
      cls := appointKindToCssClass(appointTime.kind),
      css(style => style.cursor = "pointer"),
      onclick := (onElementClick)
    )(
      div(appointTimeSpanRep),
      div(appointTimeKindRep),
      slotsElement
    )
  adjustVacantClass()

  def appointKindToCssClass(kind: String): String = {
    AppointKind(kind).cssClass
  }

  def appointTimeId: Int = appointTime.appointTimeId

  def appoints: List[Appoint] =
    slots.map(slot => slot.appoint)

  def probeVacantKind(): Option[String] =
    if hasVacancy then Some(appointTime.kind) else None

  def hasVacancy: Boolean = slots.size < appointTime.capacity

  def adjustVacantClass(): Unit =
    if hasVacancy then ele(cls := "vacant")
    else ele(cls :- "vacant")

  def addAppoint(appoint: Appoint): Unit =
    if slots.find(s => s.appoint.appointId == appoint.appointId).isEmpty then
      val slot = Slot(appoint)
      slots = slots ++ List(slot)
      slotsElement(slot.ele)
      adjustVacantClass()

  def updateAppoint(appoint: Appoint): Unit =
    slots
      .find(s => s.appoint.appointId == appoint.appointId)
      .foreach(slot => {
        slot.onAppointUpdated(appoint)
      })

  def addAppoints(appoints: Seq[Appoint]): Unit =
    appoints.foreach(addAppoint(_))

  def removeAppoint(appoint: Appoint): Unit =
    slots
      .find(s => s.appoint == appoint)
      .map(s => {
        slots = slots.filter(_ != s)
        s.ele.remove()
      })
    adjustVacantClass()

  def appointTimeSpanRep: String =
    val f = Misc.formatAppointTime(appointTime.fromTime)
    val u = Misc.formatAppointTime(appointTime.untilTime)
    val capa: String =
      if appointTime.capacity <= 1 then "" else s" (${appointTime.capacity})"
    (s"$f - $u") + capa

  def appointTimeKindRep: String =
    val label = AppointKind(appointTime.kind).label
    if label.isEmpty then "" else s"[${label}]"

  def onElementClick(event: MouseEvent): Unit =
    if slots.size < appointTime.capacity then openAppointDialog()

  def openAppointDialog(): Unit =
    MakeAppointDialog.open(appointTime)

object AppointTimeBox:
  def apply(appointTime: AppointTime, appoints: List[Appoint]): AppointTimeBox =
    val box = AppointTimeBox(appointTime)
    appoints.foreach(box.addAppoint(_))
    box
