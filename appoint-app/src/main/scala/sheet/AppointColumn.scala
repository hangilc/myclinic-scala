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
import org.scalajs.dom.{HTMLElement, MouseEvent}
import cats.syntax.all.*
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.clinicop.*
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.web.appbase.ElementDispatcher.*
import dev.myclinic.scala.web.appoint.AppEvents

case class AppointColumn(
    date: LocalDate,
    op: ClinicOperation
):
  var boxes: List[AppointTimeBox] = List.empty
  val dateElement = div()
  val vacantKindsArea = span()
  val kenshinArea = span()
  var boxWrapper = div()
  val ele = div(cls := "appoint-column", cls := op.code)(
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

  def updateUI(): Unit =
    adjustVacantClass()
    markKenshin()

  ele.addCreatedListener(
    AppEvents.publishers.appointTime,
    (event, gen) => {
      val created = event.created
      if created.date == date then 
        addAppointTime(created, gen)
        updateUI()
    }
  )
  ele.addUpdatedListener(
    AppEvents.publishers.appointTime,
    (event, gen) => {
      if event.updated.date == date then updateUI()
    }
  )
  ele.addDeletedListener(
    AppEvents.publishers.appointTime,
    (event, gen) => {
      val deleted = event.deleted
      if deleted.date == date then 
        removeAppointTime(deleted, gen)
        updateUI()
    }
  )
  ele.addCreatedListener(
    AppEvents.publishers.appoint,
    (event, gen) => 
      if hasAppointTimeId(event.created.appointTimeId) then updateUI()
  )
  ele.addUpdatedListener(
    AppEvents.publishers.appoint,
    (event, gen) => 
      if hasAppointTimeId(event.updated.appointTimeId) then updateUI()
  )
  ele.addDeletedListener(
    AppEvents.publishers.appoint,
    (event, gen) => 
      if hasAppointTimeId(event.deleted.appointTimeId) then updateUI()
  )

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

  def adjustVacantClass(): Unit =
    val kinds = probeVacantKinds()
    val wrapper = vacantKindsArea
    wrapper.clear()
    if kinds.isEmpty then dateElement(cls :- "vacant")
    else
      dateElement(cls := "vacant")
      kinds.foreach(k => {
        val icon =
          Icons.circleFilled(
            Icons.defaultStaticStyle,
            css(style => {
              style.fill = k.iconColor
            })
          )
        wrapper(icon)
      })

  def countKenshin(): Int =
    boxes.foldLeft(0)((acc, ele) => acc + ele.countKenshin())

  def markKenshin(): Unit =
    val n = countKenshin()
    println(("kenshin", n))
    kenshinArea.clear()
    if n > 0 then kenshinArea(s"健$n")

  def totalAppoints: Int =
    boxWrapper.qSelectorAllCount(".appoint-slot")

  def hasAppointTimeId(appointTimeId: Int): Boolean =
    boxes.find(_.appointTime.appointTimeId == appointTimeId).isDefined

  def composeContextMenu(
      prev: List[(String, () => Unit)]
  ): List[(String, () => Unit)] =
    prev

  def onContextMenu(event: MouseEvent): Unit =
    event.preventDefault()
    val contextMenu = composeContextMenu(List.empty)
    if !contextMenu.isEmpty then ContextMenu(contextMenu).open(event)

  def findVacantFollowers(
      appointTime: AppointTime
  ): List[AppointTime] =
    val list = boxes
      .dropWhile(b => b.appointTime.appointTimeId != appointTime.appointTimeId)
    AppointTime.extractAdjacentRunEmbedded(list, _.appointTime)._1
      .tail
      .takeWhile(a => a.numSlots == 0)
      .map(_.appointTime)

  def makeAppointTimeBox(
      appointTime: AppointTime,
      gen: Int,
      findVacantRegular: () => List[AppointTime]
  ): AppointTimeBox =
    new AppointTimeBox(appointTime, gen, () => findVacantFollowers(appointTime))

  def setAppointTimes(
      gen: Int,
      appointTimesFilled: List[(AppointTime, List[Appoint])]
  ): Unit =
    boxes = appointTimesFilled.map(item =>
      item match {
        case (appointTime, appoints) =>
          val box = makeAppointTimeBox(
            appointTime,
            gen,
            () => findVacantFollowers(appointTime)
          )
          box.setAppoints(gen, appoints)
          boxWrapper(box.ele)
          box
      }
    )
    updateUI()

  def addAppointTime(appointTime: AppointTime, gen: Int): Unit =
    val box = makeAppointTimeBox(
      appointTime,
      gen,
      () => findVacantFollowers(appointTime)
    )
    boxWrapper(box.ele)
    boxes = Types.insert(box, _.ele, boxes, boxWrapper)
    updateUI()

  def removeAppointTime(appointTime: AppointTime, gen: Int): Unit =
    boxes = Types.delete(
      _.appointTimeId == appointTime.appointTimeId,
      _.ele,
      boxes,
      boxWrapper
    )

