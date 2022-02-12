package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.model.{*, given}
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
import dev.myclinic.scala.web.appoint.sheet.appointdialog.MakeAppointDialog
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.web.appbase.{EventFetcher, SyncedComp}

class AppointTimeBox(
    _gen: Int,
    _appointTime: AppointTime,
    val findVacantFollowers: () => List[AppointTime]
)(using EventFetcher) extends SyncedComp[AppointTime](_gen, _appointTime):
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

  ele.addCreatedListener[Appoint](event => {
    val created = event.dataAs[Appoint]
    addAppoint(event.appEventId, created)
  })
  ele.addDeletedAllListener[Appoint](event => {
    val deleted = event.dataAs[Appoint]
    if deleted.appointTimeId == appointTime.appointTimeId then
      slots = slots.filter(_.appoint.appointTimeId != deleted.appointTimeId)
  })

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
      val slot = Slot(gen, appoint)
      slotsElement(slot.ele)
      slot
    })
    adjustVacantClass()

  def addAppoint(gen: Int, appoint: Appoint): Unit =
    slots = Types.insert(Slot(gen, appoint), _.ele, slots, slotsElement)
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
    MakeAppointDialog(appointTime, () => findVacantFollowers().headOption)
      .open()

  def doDeleteAppointTime(): Unit =
    System.err.println("doDeleteAppointTime not implemented.")

  def countKenshin(): Int =
    slots.foldLeft(0)((acc, ele) => {
      if ele.appoint.hasTag("健診") then acc + 1 else acc
    })

object AppointTimeBox:
  given Ordering[AppointTimeBox] = Ordering.by(_.appointTime)
