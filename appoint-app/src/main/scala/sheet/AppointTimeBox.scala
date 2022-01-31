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

class AppointTimeBox(
    var appointTime: AppointTime,
    gen: Int,
    val findVacantFollowers: () => List[AppointTime]
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

  object Slot:
    given Ordering[Slot] = Ordering.by(_.appoint.appointId)

  var slots: List[Slot] = List.empty
  val eTimeRep = div
  val eKindRep = div
  val slotsElement = div
  var kindCssClass = ""
  val ele =
    div(
      cls := "appoint-time-box",
      cls := s"appoint-time-id-${appointTime.appointTimeId}",
      css(style => style.cursor = "pointer"),
      onclick := (onElementClick)
    )(eTimeRep, eKindRep, slotsElement)
  updateUI()

  ele.addCreatedHandler(
    AppEvents.publishers.appoint,
    (evt, gen) => {
      addAppoint(evt.created, gen)
    }
  )
  ele.addDeletedHandler(
    AppEvents.publishers.appoint,
    (evt, gen) => {
      deleteAppoint(evt.deleted, gen)
    }
  )
  ele.addUpdatedWithIdListener(
    AppEvents.publishers.appointTime,
    appointTime.appointTimeId,
    (evt, gen) => {
      appointTime = evt.updated
      updateUI()
    }
  )

  def updateUI(): Unit =
    if !kindCssClass.isEmpty then ele(cls :- kindCssClass)
    kindCssClass = appointKindToCssClass(appointTime.kind)
    ele(
      cls := (if !kindCssClass.isEmpty then Some(kindCssClass) else None),
      cls := (if appointTime.capacity > 0 then Some("vacant") else None)
    )
    eTimeRep(innerText := appointTimeSpanRep)
    eKindRep(innerText := appointTimeKindRep)
    adjustVacantClass()

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

  def numSlots: Int =
    ele.qSelectorAllCount(".appoint-slot")

  def setAppoints(gen: Int, appoints: List[Appoint]): Unit =
    slots = appoints.map(appoint => {
      val slot = Slot(appoint)
      slotsElement(slot.ele)
      slot
    })
    adjustVacantClass()

  def addAppoint(appoint: Appoint, gen: Int): Unit =
    slots = Types.insert(Slot(appoint), _.ele, slots, slotsElement)
    adjustVacantClass()

  def deleteAppoint(appoint: Appoint, gen: Int): Unit =
    slots = Types.delete(_.appoint.appointId == appoint.appointId, _.ele, slots, slotsElement)
    adjustVacantClass()

  def updateAppoint(appoint: Appoint): Unit =
    slots
      .find(s => s.appoint.appointId == appoint.appointId)
      .foreach(slot => {
        slot.onAppointUpdated(appoint)
      })

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
    MakeAppointDialog(appointTime, () => findVacantFollowers().headOption).open()

  def doDeleteAppointTime(): Unit =
    System.err.println("doDeleteAppointTime not implemented.")

  def countKenshin(): Int =
    slots.foldLeft(0)((acc, ele) => {
      if ele.appoint.hasTag("健診") then acc + 1 else acc
    })

object AppointTimeBox:
  given Ordering[AppointTimeBox] = Ordering.by(_.appointTime)
