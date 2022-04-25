package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{*, given}
import dev.myclinic.scala.web.appbase.{SyncedDataSource2, EventFetcher}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.appoint.sheet.appointdialog.EditAppointDialog
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appoint.CustomEvents

case class Slot(dsrc: SyncedDataSource2[Appoint, AppointTime]):
  def appoint: Appoint = dsrc.data._1
  def appointTime: AppointTime = dsrc.data._2
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
  updateUI()
  dsrc.onUpdate(_ => updateUI())
  dsrc.onDelete(_ => {
    val parent = ele.parentElement
    ele.remove()
    CustomEvents.appointPostDeleted.trigger(parent, appoint, true)
  })
  dsrc.startSync(ele)

  def updateUI(): Unit =
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

  given Comp[Slot] with
    def ele(c: Slot): HTMLElement = c.ele
