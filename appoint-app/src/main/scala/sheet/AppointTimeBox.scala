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
import scala.math.Ordered.orderingToOrdered
import java.time.LocalDate
import dev.myclinic.scala.web.appoint.sheet.appointdialog.MakeAppointDialog
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.web.appbase.*
import dev.myclinic.scala.web.appoint.CustomEvents

class AppointTimeBox(
    val dsrc: SyncedDataSource[AppointTime],
    val findVacantFollowers: () => List[AppointTime]
)(using EventFetcher):
  import AppointTimeBox.*
  def appointTime: AppointTime = dsrc.data
  private var slots: List[Slot] = List.empty
  val eTimeRep = div
  val eKindRep = div
  val slotsWrapper = div
  var kindCssClass = ""
  val ele =
    div(
      cls := "appoint-time-box",
      cls := s"appoint-time-id-${appointTime.appointTimeId}",
      css(style => style.cursor = "pointer")
    )(eTimeRep, eKindRep, slotsWrapper)
  ele(onclick := (onElementClick _))
  CustomEvents.appointPostDeleted.handle(
    ele,
    deleted => {
      val id = appointTime.appointTimeId
      slots = CompList.delete(slots, _.appointTime.appointTimeId == id)
      adjustUI()
    }
  )
  updateUI()
  dsrc.onUpdate(updateUI _)
  dsrc.onDelete(() => {
    val parent = ele.parentElement
    ele.remove()
    CustomEvents.appointTimePostDeleted.trigger(parent, appointTime, true)
  })
  dsrc.startSync(ele)

  def numSlots: Int = slots.size

  private def updateUI(): Unit =
    if !kindCssClass.isEmpty then ele(cls :- kindCssClass)
    kindCssClass = appointKindToCssClass(appointTime.kind)
    ele(
      cls := (if !kindCssClass.isEmpty then Some(kindCssClass) else None),
      cls := (if appointTime.capacity > 0 then Some("vacant") else None)
    )
    eTimeRep(innerText := appointTimeSpanRep(appointTime))
    eKindRep(innerText := appointTimeKindRep(appointTime))
    adjustUI()

  def adjustUI(): Unit =
    adjustVacantClass()

  def hasVacancy: Boolean = numSlots < appointTime.capacity

  def addAppoints(g: Int, appoints: List[Appoint]): Unit =
    appoints.foreach(appoint => {
      SyncedDataSource.syncGen(dsrc.gen, dsrc.data, g, appoint) match {
        case Some(g, appointTime, appoint) =>
          val dsrc2 = SyncedDataSource2(g, appoint, appointTime)
          val slot = Slot(dsrc2)
          slots = CompList.append(slots, slot, slotsWrapper)
        case None => ()
      }
    })
    adjustUI()

  def probeVacantKind(): Option[String] =
    if hasVacancy then Some(appointTime.kind) else None

  private def adjustVacantClass(): Unit =
    if hasVacancy then ele(cls := "vacant")
    else ele(cls :- "vacant")

  def countKenshin(): Int =
    slots.foldLeft(0)((acc, ele) => {
      if ele.appoint.hasTag("健診") then acc + 1 else acc
    })

  def findVacantRegular(): Option[AppointTime] =
    findVacantFollowers().headOption

  private def onElementClick(): Unit =
    if numSlots < appointTime.capacity then
      openAppointDialog(appointTime, findVacantRegular _)

object AppointTimeBox:
  def appointKindToCssClass(kind: String): String = {
    AppointKind(kind).cssClass
  }

  def appointTimeSpanRep(appointTime: AppointTime): String =
    val f = Misc.formatAppointTime(appointTime.fromTime)
    val u = Misc.formatAppointTime(appointTime.untilTime)
    val capa: String =
      if appointTime.capacity <= 1 then "" else s" (${appointTime.capacity})"
    (s"$f - $u") + capa

  def appointTimeKindRep(appointTime: AppointTime): String =
    val label = AppointKind(appointTime.kind).label
    if label.isEmpty then "" else s"[${label}]"

  def openAppointDialog(
      appointTime: AppointTime,
      fVacantFollower: () => Option[AppointTime]
  ): Unit =
    MakeAppointDialog(appointTime, fVacantFollower)
      .open()

  given Ordering[AppointTimeBox] = Ordering.by(_.appointTime)

  given CompData[AppointTimeBox, AppointTime] with
    def ele(c: AppointTimeBox) = c.ele
    def data(c: AppointTimeBox) = c.appointTime

// class AppointTimeBoxOrig(
//     var gen: Int,
//     var appointTime: AppointTime,
//     val findVacantFollowers: () => List[AppointTime]
// )(using EventFetcher, SyncedComp2[Slot, Appoint, AppointTime]):
//   var slots: List[Slot] = List.empty
//   val eTimeRep = div
//   val eKindRep = div
//   val slotsElement = div
//   var kindCssClass = ""
//   val ele =
//     div(
//       cls := "appoint-time-box",
//       cls := s"appoint-time-id-${appointTime.appointTimeId}",
//       css(style => style.cursor = "pointer"),
//       onclick := (onElementClick)
//     )(eTimeRep, eKindRep, slotsElement)

//   def updateUI(_gen: Int, _appointTime: AppointTime): Unit =
//     gen = _gen
//     appointTime = _appointTime
//     updateUI()

//   def updateUI(): Unit =
//     if !kindCssClass.isEmpty then ele(cls :- kindCssClass)
//     kindCssClass = appointKindToCssClass(appointTime.kind)
//     ele(
//       cls := (if !kindCssClass.isEmpty then Some(kindCssClass) else None),
//       cls := (if appointTime.capacity > 0 then Some("vacant") else None)
//     )
//     eTimeRep(innerText := appointTimeSpanRep)
//     eKindRep(innerText := appointTimeKindRep)
//     adjustVacantClass()

//   def createSlot(genAppoint: Int, appoint: Appoint): Option[Slot] =
//     SyncedComp2.createSynced(genAppoint, appoint, gen, appointTime)

//   def addAppoints(gen: Int, appoints: List[Appoint]): Unit =
//     appoints.foreach(appoint => {
//       createSlot(gen, appoint) match {
//         case Some(slot) => slots = CompList.append(slots, slot, slotsElement)
//         case None       => ()
//       }
//     })
//     adjustVacantClass()

//   def addAppoint(gen: Int, appoint: Appoint): Unit =
//     addAppoints(gen, List(appoint))

//   def appointKindToCssClass(kind: String): String = {
//     AppointKind(kind).cssClass
//   }

//   def appointTimeId: Int = appointTime.appointTimeId

//   def appoints: List[Appoint] =
//     slots.map(slot => slot.appoint)

//   def probeVacantKind(): Option[String] =
//     if hasVacancy then Some(appointTime.kind) else None

//   def hasVacancy: Boolean = numSlots < appointTime.capacity

//   def adjustVacantClass(): Unit =
//     if hasVacancy then ele(cls := "vacant")
//     else ele(cls :- "vacant")

//   def numSlots: Int =
//     ele.qSelectorAllCount(".appoint-slot")

//   def appointTimeSpanRep: String =
//     val f = Misc.formatAppointTime(appointTime.fromTime)
//     val u = Misc.formatAppointTime(appointTime.untilTime)
//     val capa: String =
//       if appointTime.capacity <= 1 then "" else s" (${appointTime.capacity})"
//     (s"$f - $u") + capa

//   def appointTimeKindRep: String =
//     val label = AppointKind(appointTime.kind).label
//     if label.isEmpty then "" else s"[${label}]"

//   def onElementClick(event: MouseEvent): Unit =
//     if numSlots < appointTime.capacity then openAppointDialog()

//   def openAppointDialog(): Unit =
//     MakeAppointDialog(appointTime, () => findVacantFollowers().headOption)
//       .open()

//   def doDeleteAppointTime(): Unit =
//     System.err.println("doDeleteAppointTime not implemented.")

//   def countKenshin(): Int =
//     slots.foldLeft(0)((acc, ele) => {
//       if ele.appoint.hasTag("健診") then acc + 1 else acc
//     })
