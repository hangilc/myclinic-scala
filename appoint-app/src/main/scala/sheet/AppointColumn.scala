package dev.myclinic.scala.web.appoint.sheet

import java.time.{LocalTime, LocalDate}
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint
import scala.language.implicitConversions

case class AppointColumn(
    date: LocalDate,
    appointMap: Map[AppointColumn.AppointTimeId, List[Appoint]],
    appointTimeBoxMaker: (AppointTime, List[Appoint]) => AppointTimeBox
):
  var boxElement = div()
  var boxes: List[AppointTimeBox] = List.empty
  val ele = div(cls := "col-2")(
    div(dateRep),
    boxElement
  )

  def dateRep: String = Misc.formatAppointDate(date)

  def setAppointTimes(appointTimes: List[AppointTime]): Unit =
    appointTimes.map(a => {
      val box = appointTimeBoxMaker(
        a,
        appointMap.getOrElse(a.appointTimeId, List.empty)
      )
      boxes = boxes :+ box
      boxElement(box.ele)
    })

  def deleteAppointTime(appointTimeId: Int): Unit =
    boxes.find(box => box.appointTime.appointTimeId == appointTimeId)
      .foreach(box => {
        boxes = boxes.filterNot(_ == box)
        box.ele.remove()
      })

  def updateAppointTime(updated: AppointTime): Unit =
    boxes.find(box => box.appointTime.appointTimeId == updated.appointTimeId)
      .foreach(box => {
        val newBox = appointTimeBoxMaker(updated, box.appoints)
        boxes = boxes.map(b => if b == box then newBox else b)
        box.ele.replaceBy(newBox.ele)
      })

  private def findBoxByAppoint(appoint: Appoint): Option[AppointTimeBox] =
    boxes.find(b => b.appointTime.appointTimeId == appoint.appointTimeId)

  def insert(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).map(_.addAppoint(appoint))

  def delete(appoint: Appoint): Unit =
    findBoxByAppoint(appoint).map(_.removeAppoint(appoint))

object AppointColumn:
  type AppointTimeId = Int

  def create(
      appointDate: AppointDate,
      appointMap: Map[AppointTimeId, List[Appoint]],
      appointTimeBoxMaker: (AppointTime, List[Appoint]) => AppointTimeBox
  ): AppointColumn =
    val c = AppointColumn(appointDate.date, appointMap, appointTimeBoxMaker)
    c.setAppointTimes(appointDate.appointTimes)
    c
