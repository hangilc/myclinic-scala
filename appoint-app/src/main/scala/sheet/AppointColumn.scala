package dev.myclinic.scala.web.appoint.sheet

import java.time.{LocalTime, LocalDate}
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ContextMenu, ShowMessage}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.sheet.Types.SortedElement
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import cats.syntax.all.*
import dev.myclinic.scala.webclient.Api
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.clinicop.*

given Ordering[AppointColumn] with
  def compare(a: AppointColumn, b: AppointColumn): Int =
    summon[Ordering[LocalDate]].compare(a.date, b.date)

val sortedAppointColumn = new SortedElement[AppointColumn]:
  def element(a: AppointColumn): HTMLElement = a.ele

case class AppointColumn(
    date: LocalDate,
    op: ClinicOperation,
    appointTimeBoxMaker: AppointTime => AppointTimeBox
):
  var boxes: Seq[AppointTimeBox] = Vector.empty
  val dateElement = div()
  val vacantKindsArea = span()
  val kenshinArea = span()
  var boxWrapper = div()
  val ele = div(cls := "date-column", cls := op.code)(
    dateElement(cls := "date")(
      dateRep,
      vacantKindsArea,
      kenshinArea(cls := "kenshin-area"),
      div(cls := "date-label")(
        ClinicOperation.getLabel(op)
      ),
      oncontextmenu := (onContextMenu _)
    ),
    boxWrapper
  )
  adjustVacantClass()

  def dateRep: String = Misc.formatAppointDate(date)

  def probeVacantKinds(): List[AppointKind] =
    boxes
      .map(b => b.probeVacantKind())
      .filter(_ != None)
      .sequence
      .map(_.toSet)
      .getOrElse(Set.empty)
      .toList
      .map(AppointKind(_))
      .sortBy(_.ord)

  def hasVacancy: Boolean = boxes.find(_.hasVacancy).isDefined

  def adjustVacantClass(): Unit =
    val kinds = probeVacantKinds()
    val wrapper = vacantKindsArea
    wrapper.clear()
    if kinds.isEmpty then
      dateElement(cls :- "vacant")
    else
      dateElement(cls := "vacant")
      kinds.foreach(k => {
        val icon = Icons.circleFilled(color = k.iconColor)(Icons.defaultStaticStyle)
        wrapper(icon)
      })
  
  def totalAppoints: Int =
    boxes.foldLeft(0)((acc, ele) => acc + ele.slots.size)

  def onContextMenu(event: MouseEvent): Unit =
    event.preventDefault
    var menu: List[(String, () => Unit)] = List.empty
    if totalAppoints == 0 then
      menu = menu :+ ("予約枠全削除" -> (doDeleteAllAppointTimes _))
    if !menu.isEmpty then
      ContextMenu(menu).open(event)

  def doDeleteAllAppointTimes(): Unit =
    val dateRep = Misc.formatAppointDate(date)
    ShowMessage.confirm(s"${dateRep}の予約枠を全部削除していいですか？", yes => {
      if yes then
        val ids: List[Int] = boxes.map(_.appointTimeId).toList
        for
          _ <- ids.map(id => Api.deleteAppointTime(id)).sequence.void
        yield ()
    })
    
  def hasAppointTimeId(appointTimeId: Int): Boolean =
    boxes.find(b => b.appointTime.appointTimeId == appointTimeId).isDefined

  def addAppointTime(appointTime: AppointTime): Unit =
    val box = appointTimeBoxMaker(appointTime)
    boxes = sortedAppointTimeBox.insert(box, boxes, boxWrapper)
    adjustVacantClass()

  def deleteAppointTime(appointTimeId: Int): Unit =
    boxes =
      sortedAppointTimeBox.remove(b => b.appointTimeId == appointTimeId, boxes)
    adjustVacantClass()

  def updateAppointTime(updated: AppointTime): Unit =
    val box = appointTimeBoxMaker(updated)
    boxes = sortedAppointTimeBox.update(
      b => {
        if b.appointTimeId == updated.appointTimeId then
          box.addAppoints(b.appoints)
          true
        else false
      },
      box,
      boxes
    )
    adjustVacantClass()

  private def findBoxByAppoint(appoint: Appoint): Option[AppointTimeBox] =
    boxes.find(b => b.appointTime.appointTimeId == appoint.appointTimeId)

  def addAppoint(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).foreach(b => b.addAppoint(appoint))
    adjustVacantClass()
    markKenshin()

  def addAppoints(appoints: Seq[Appoint]): Unit =
    appoints.foreach(addAppoint(_))

  def updateAppoint(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).foreach(b => b.updateAppoint(appoint))
    markKenshin()

  def deleteAppoint(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).foreach(b => b.removeAppoint(appoint))
    adjustVacantClass()
    markKenshin()

  def countKenshin(): Int =
    boxes.foldLeft(0)((acc, ele) => acc + ele.countKenshin())

  def markKenshin(): Unit =
    val n = countKenshin()
    kenshinArea.clear()
    if n > 0 then
      kenshinArea(s"健$n")

object AppointColumn:
  type AppointTimeId = Int

  def create(
      date: LocalDate,
      op: ClinicOperation,
      list: List[(AppointTime, List[Appoint])],
      appointTimeBoxMaker: AppointTime => AppointTimeBox
  ): AppointColumn =
    val c = AppointColumn(date, op, appointTimeBoxMaker)
    list.foreach {
      case (appointTime, appoints) => {
        c.addAppointTime(appointTime)
        c.addAppoints(appoints)
      }
    }
    c.markKenshin()
    c
