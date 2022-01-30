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


// val sortedAppointColumn = new SortedElement[AppointColumn]:
//   def element(a: AppointColumn): HTMLElement = a.ele

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
  ele.addCreatedListener(
    AppEvents.publishers.appointTime,
    (event, gen) => {
      val created = event.created
      if created.date == date then
        ???
    }
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

  def hasVacancy: Boolean = boxes.find(_.hasVacancy).isDefined

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

  def totalAppoints: Int =
    boxWrapper.qSelectorAllCount(".appoint-slot")

  def composeContextMenu(prev: List[(String, () => Unit)]): List[(String, () => Unit)] =
    prev

  def onContextMenu(event: MouseEvent): Unit =
    event.preventDefault()
    val contextMenu = composeContextMenu(List.empty)
    if !contextMenu.isEmpty then ContextMenu(contextMenu).open(event)

  def findFollowingVacantRegular(
      appointTime: AppointTime
  ): Option[AppointTime] =
    val idx = boxes.indexWhere(b =>
      b.appointTime.appointTimeId == appointTime.appointTimeId
    )
    if idx < 0 || (idx + 1) >= boxes.size then None
    else
      val f = boxes(idx + 1)
      if f.appointTime.kind == "regular" && f.hasVacancy then
        Some(f.appointTime)
      else None

  def hasAppointTimeId(appointTimeId: Int): Boolean =
    boxes.find(b => b.appointTime.appointTimeId == appointTimeId).isDefined

  def makeAppointTimeBox(
      appointTime: AppointTime,
      findVacantRegular: () => Option[AppointTime]
  ): AppointTimeBox =
    new AppointTimeBox(appointTime, findVacantRegular)

  def setAppointTimes(
      gen: Int,
      appointTimesFilled: List[(AppointTime, List[Appoint])]
  ): Unit =
    boxes = appointTimesFilled.map((appointTime, appoints) => {
      val box = makeAppointTimeBox(
        appointTime,
        () => findFollowingVacantRegular(appointTime)
      )
      box.setAppoints(gen, appoints)
      boxWrapper(box.ele)
      box
    })
    adjustVacantClass()

  def addAppointTime(appointTime: AppointTime): Unit =
    val box = makeAppointTimeBox(
      appointTime,
      () => findFollowingVacantRegular(appointTime)
    )
    boxWrapper(box.ele)
    boxes = boxes :+ box
    adjustVacantClass()

  def deleteAppointTime(appointTimeId: Int): Unit =
    boxes.find(_.appointTime.appointTimeId == appointTimeId).foreach(box => {
      box.ele.remove()
      boxes = boxes.filterNot(_ == box)
    })
    adjustVacantClass()

  // def updateAppointTime(updated: AppointTime): Unit =
  //   val box =
  //     appointTimeBoxMaker(updated, () => findFollowingVacantRegular(updated))
  //   boxes = sortedAppointTimeBox.update(
  //     b => {
  //       if b.appointTimeId == updated.appointTimeId then
  //         box.addAppoints(b.appoints)
  //         true
  //       else false
  //     },
  //     box,
  //     boxes
  //   )
  //   adjustVacantClass()

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
    if n > 0 then kenshinArea(s"健$n")

// object AppointColumn:
//   type AppointTimeId = Int

//   def create(
//       date: LocalDate,
//       op: ClinicOperation,
//       list: List[(AppointTime, List[Appoint])],
//       appointTimeBoxMaker: (
//           AppointTime,
//           () => Option[AppointTime]
//       ) => AppointTimeBox
//   ): AppointColumn =
//     val c = AppointColumn(date, op, appointTimeBoxMaker)
//     list.foreach {
//       case (appointTime, appoints) => {
//         c.addAppointTime(appointTime)
//         c.addAppoints(appoints)
//       }
//     }
//     c.markKenshin()
//     c

object AppointColumn:
  given Ordering[AppointColumn] with
    def compare(a: AppointColumn, b: AppointColumn): Int =
      summon[Ordering[LocalDate]].compare(a.date, b.date)
