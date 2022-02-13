package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{*, given}
import dev.myclinic.scala.web.appbase.{SyncedComp, EventFetcher}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.appoint.sheet.appointdialog.EditAppointDialog
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.web.appbase.SyncedComp2
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.Comp

case class Slot(
    var gen: Int,
    var appoint: Appoint,
    var appointTime: AppointTime
):
  val eLabel = div()
  val eTags = div()
  val ele = div(
    cls := "appoint-slot",
    cls := s"appoint-id-${appoint.appointId}"
  )(onclick := (onClick _))(
    eLabel,
    eTags
  )
  var dialog: Option[EditAppointDialog] = None

  def updateUI(_gen: Int, _appoint: Appoint, _appointTime: AppointTime): Unit =
    gen = _gen
    appoint = _appoint
    appointTime = _appointTime
    eLabel(clear, label)
    eTags(clear, tagsRep)

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

object Slot:
  given Ordering[Slot] = Ordering.by(_.appoint.appointId)

  given SyncedComp2[Slot, Appoint, AppointTime] with
    def create(gen: Int, appoint: Appoint, appointTime: AppointTime): Slot =
      Slot(gen, appoint, appointTime)
    def ele(c: Slot): HTMLElement = c.ele
    def updateUI(c: Slot, gen: Int, appoint: Appoint, appointTime: AppointTime): Unit =
      c.updateUI(gen, appoint, appointTime)

  given Comp[Slot] with
    def ele(c: Slot): HTMLElement = c.ele
