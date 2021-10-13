package dev.myclinic.scala.web.appoint.sheet

import java.time.{LocalTime, LocalDate}
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint
import scala.language.implicitConversions
import dev.myclinic.scala.web.appoint.sheet.Types.SortedElement
import org.scalajs.dom.raw.HTMLElement

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
  var boxWrapper = div()
  val ele = div(css(style => style.width = "120px"))(
    div(dateRep),
    boxWrapper
  )

  def dateRep: String = Misc.formatAppointDate(date)

  def hasAppointTimeId(appointTimeId: Int): Boolean =
    boxes.find(b => b.appointTime.appointTimeId == appointTimeId).isDefined

  def addAppointTime(appointTime: AppointTime): Unit =
    val box = appointTimeBoxMaker(appointTime)
    boxes = sortedAppointTimeBox.insert(box, boxes, boxWrapper)

  def deleteAppointTime(appointTimeId: Int): Unit =
    boxes =
      sortedAppointTimeBox.remove(b => b.appointTimeId == appointTimeId, boxes)

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

  def addAppoint(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).foreach(b => b.addAppoint(appoint))

  def addAppoints(appoints: Seq[Appoint]): Unit =
    appoints.foreach(addAppoint(_))

  private def findBoxByAppoint(appoint: Appoint): Option[AppointTimeBox] =
    boxes.find(b => b.appointTime.appointTimeId == appoint.appointTimeId)

  def insert(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).map(_.addAppoint(appoint))

  def delete(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).map(_.removeAppoint(appoint))

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
