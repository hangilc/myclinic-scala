package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.model.*
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Html.{given, *}
import dev.fujiwara.domq.Modifiers.{given, *}
import dev.fujiwara.domq.{ContextMenu}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.EventTarget
import org.scalajs.dom.document
import dev.myclinic.scala.util.DateTimeOrdering.given
import scala.math.Ordered.orderingToOrdered
import java.time.LocalDate
import dev.myclinic.scala.web.appoint.sheet.Types.SortedElement
import dev.myclinic.scala.web.appoint.sheet.appointdialog.EditAppointDialog
import dev.myclinic.scala.web.appoint.sheet.appointdialog.MakeAppointDialog
import dev.myclinic.scala.web.appoint.AppEvents
import dev.myclinic.scala.web.appbase.ElementDispatcher.*

given Ordering[AppointTimeBox] with
  def compare(a: AppointTimeBox, b: AppointTimeBox): Int =
    summon[Ordering[AppointTime]].compare(a.appointTime, b.appointTime)

val sortedAppointTimeBox: SortedElement[AppointTimeBox] =
  new SortedElement[AppointTimeBox]:
    def element(a: AppointTimeBox): HTMLElement = a.ele

case class AppointTimeBox(
    appointTime: AppointTime,
    followingVacantRegular: () => Option[AppointTime]
):
  case class Slot(var appoint: Appoint):
    val eLabel = div()
    val eTags = div()
    val ele = div(
      cls := "appoint-slot",
      cls := s"appoint-id-${appoint.appointId}"
      )(onclick := (onClick _))(eLabel, eTags)
    var dialog: Option[EditAppointDialog] = None
    updateUI()

    def updateUI(): Unit =
      eLabel.clear()
      eLabel(label)
      eTags.clear()
      eTags(tagsRep)
    def label: String =
      val patientId: String =
        if appoint.patientId == 0 then ""
        else s"(${appoint.patientId}) "
      val name: String = s"${appoint.patientName}"
      val memo: String =
        if appoint.memoString.isEmpty then "" else s" （${appoint.memoString}）"
      patientId + name + memo
    def tagsRep: String =
      appoint.tags.mkString("、")
    def onClick(event: MouseEvent): Unit =
      event.stopPropagation()
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
      cls := "appoint-time-box vacant",
      cls := s"appoint-time-id-${appointTime.appointTimeId}",
      cls := appointKindToCssClass(appointTime.kind),
      cls := (if appointTime.capacity > 0 then Some("vacant") else None),
      css(style => style.cursor = "pointer"),
      onclick := (onElementClick)
    )(
      div(appointTimeSpanRep),
      div(appointTimeKindRep),
      slotsElement
    )
  ele.addCreatedHandler(AppEvents.publishers.appoint, (evt, gen) => {
    addAppoint(evt.created, gen)
  })
  ele.addDeletedHandler(AppEvents.publishers.appoint, (evt, gen) => {
    deleteAppoint(evt.deleted, gen)
  })

  def appointKindToCssClass(kind: String): String = {
    AppointKind(kind).cssClass
  }

  def appointTimeId: Int = appointTime.appointTimeId

  def appoints: List[Appoint] =
    slots.map(slot => slot.appoint)

  def probeVacantKind(): Option[String] =
    if hasVacancy then Some(appointTime.kind) else None

  def hasVacancy: Boolean = numSlots < appointTime.capacity

  def adjustVacantClass(): Unit =
    if hasVacancy then ele(cls := "vacant")
    else ele(cls :- "vacant")

  def setAppoints(gen: Int, appoints: List[Appoint]): Unit =
    appoints.foreach(appoint => {
      val slot = Slot(appoint)
      slotsElement(slot.ele)
    })
    adjustVacantClass()

  def numSlots: Int =
    ele.qSelectorAllCount(".appoint-slot")

  def addAppoint(appoint: Appoint, gen: Int): Unit =
    val slot = Slot(appoint)
    slotsElement(slot.ele)
    adjustVacantClass()

  def deleteAppoint(appoint: Appoint, gen: Int): Unit =
    ele.qSelector(s".appoint-slot.appoint-id-${appoint.appointId}").foreach(_.remove())
    adjustVacantClass()

  def addAppoint(appoint: Appoint): Unit =
    val slot = Slot(appoint)
    slotsElement(slot.ele)
    if numSlots >= appointTime.capacity then ele(cls :- "vacant")
    // if slots.find(s => s.appoint.appointId == appoint.appointId).isEmpty then
    //   val slot = Slot(appoint)
    //   slots = slots ++ List(slot)
    //   slotsElement(slot.ele)
    //   adjustVacantClass()

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
    if numSlots < appointTime.capacity then openAppointDialog()

  def openAppointDialog(): Unit =
    MakeAppointDialog(appointTime, followingVacantRegular).open()

  def doDeleteAppointTime(): Unit =
    System.err.println("doDeleteAppointTime not implemented.")

  def countKenshin(): Int =
    slots.foldLeft(0)((acc, ele) => {
      if ele.appoint.hasTag("健診") then acc + 1 else acc
    })

object AppointTimeBox:
  def apply(
      appointTime: AppointTime,
      appoints: List[Appoint],
      followingVacantRegular: () => Option[AppointTime]
  ): AppointTimeBox =
    val box = AppointTimeBox(appointTime, followingVacantRegular)
    appoints.foreach(box.addAppoint(_))
    box
