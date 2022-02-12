package dev.myclinic.scala.web.reception.sheet

import dev.myclinic.scala.model.{*, given}
import dev.myclinic.scala.web.appbase.{SyncedComp, EventFetcher}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.appoint.sheet.appointdialog.EditAppointDialog
import org.scalajs.dom.MouseEvent

case class Slot(_gen: Int, _appoint: Appoint)(using EventFetcher)
    extends SyncedComp[Appoint](_gen, _appoint):
  val eLabel = div()
  val eTags = div()
  val ele = div(
    cls := "appoint-slot",
    cls := s"appoint-id-${_appoint.appointId}"
  )(onclick := (onClick _))(eLabel, eTags)
  var dialog: Option[EditAppointDialog] = None
  initSyncedComp()

  def updateUI(): Unit =
    eLabel(clear, label)
    eTags(clear, tagsRep)
  def appoint: Appoint = currentData
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



