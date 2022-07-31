package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.model.{*, given}
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import dev.fujiwara.domq.all.{*, given}
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
  protected var slots: List[Slot] = List.empty
  val eTimeRep = div
  val eKindRep = div
  val slotsWrapper = div
  var kindCssClass = ""
  val ele =
    div(
      cls := "appoint-time-box",
      cls := s"appoint-time-id-${appointTime.appointTimeId}",
      css(style => style.cursor = "pointer"),
      attr("data-time") := attrDataTime
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
  CustomEvents.appointCreated.handle(
    ele,
    {
      case (g, created) => {
        addAppoints(g, List(created))
        CustomEvents.appointPostCreated.trigger(ele, created, true)
      }
    }
  )
  updateUI()
  dsrc.onUpdate(_ => updateUI())
  dsrc.onUpdate(_ => CustomEvents.appointTimePostUpdated.trigger(ele, appointTime, true))
  dsrc.onDelete(_ => {
    val parent = ele.parentElement
    ele.remove()
    CustomEvents.appointTimePostDeleted.trigger(parent, appointTime, true)
  })
  dsrc.startSync(ele)

  def numSlots: Int = slots.size

  private def attrDataTime: String =
    val from = Misc.formatAppointTime(appointTime.fromTime)
    val upto = Misc.formatAppointTime(appointTime.untilTime)
    s"${from}-${upto}"

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
  given Comp[AppointTimeBox] = _.ele
  given Dispose[AppointTimeBox] = 
    Dispose.nop[AppointTimeBox]
