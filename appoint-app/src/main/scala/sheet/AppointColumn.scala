package dev.myclinic.scala.web.appoint.sheet

import java.time.{LocalTime, LocalDate}
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.sheet.Types.SortedElement
import org.scalajs.dom.raw.HTMLElement
import cats.syntax.all.*

given Ordering[AppointColumn] with
  def compare(a: AppointColumn, b: AppointColumn): Int =
    summon[Ordering[LocalDate]].compare(a.date, b.date)

val sortedAppointColumn = new SortedElement[AppointColumn]:
  def element(a: AppointColumn): HTMLElement = a.ele

case class AppointColumn(
    date: LocalDate,
    appointTimeBoxMaker: AppointTime => AppointTimeBox
):
  var boxes: Seq[AppointTimeBox] = Vector.empty
  val dateElement = div()
  val vacantKindsArea = span()
  var boxWrapper = div()
  val ele = div(cls := "date-column")(
    dateElement(cls := "date")(
      dateRep,
      vacantKindsArea
    ),
    boxWrapper
  )
  adjustVacantClass()

  def dateRep: String = Misc.formatAppointDate(date)

  def probeVacantKinds(): List[String] =
    boxes
      .map(b => b.probeVacantKind())
      .filter(_ != None)
      .sequence
      .map(_.toSet)
      .getOrElse(Set.empty)
      .toList
      .sortBy(a => kindToOrd(a))

  def hasVacancy: Boolean = boxes.find(_.hasVacancy).isDefined

  def kindToOrd(kind: String): Int =
    kind match {
      case "regular" => 0
      case "covid-vac" => 1
      case "flu-vac" => 2
      case _ => 3
    }

  def kindToColor(kind: String): String =
    kind match {
      case "regular" => "green"
      case "covid-vac" => "purple"
      case "flu-vac" => "orange"
      case _ => "gray"
    }

  def adjustVacantClass(): Unit =
    val kinds = probeVacantKinds()
    val wrapper = vacantKindsArea
    wrapper.clear()
    if kinds.isEmpty then
      dateElement(cls :- "vacant")
    else
      dateElement(cls := "vacant")
      kinds.foreach(k => {
        val c = kindToColor(k)
        val icon = Icons.circleFilled(color = c)(Icons.defaultStaticStyle)
        wrapper(icon)
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

  def addAppoints(appoints: Seq[Appoint]): Unit =
    appoints.foreach(addAppoint(_))

  def updateAppoint(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).foreach(b => b.updateAppoint(appoint))

  def deleteAppoint(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).foreach(b => b.removeAppoint(appoint))
    adjustVacantClass()

object AppointColumn:
  type AppointTimeId = Int

  def create(
      date: LocalDate,
      list: List[(AppointTime, List[Appoint])],
      appointTimeBoxMaker: AppointTime => AppointTimeBox
  ): AppointColumn =
    val c = AppointColumn(date, appointTimeBoxMaker)
    list.foreach {
      case (appointTime, appoints) => {
        c.addAppointTime(appointTime)
        c.addAppoints(appoints)
      }
    }
    c
