package dev.myclinic.scala.web.appoint.sheet

import java.time.{LocalTime, LocalDate}
import dev.myclinic.scala.model.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ContextMenu, ShowMessage}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, MouseEvent}
import cats.syntax.all.*
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.web.appbase.{EventFetcher, SyncedComp}
import dev.myclinic.scala.clinicop.*
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.SortedCompList
import scala.math.Ordered.orderingToOrdered
import dev.myclinic.scala.web.appoint.CustomEvents

case class AppointColumn(date: LocalDate, op: ClinicOperation)(using
    EventFetcher
):
  import AppointColumn.*
  var boxes: List[AppointTimeBox] = List.empty
  val dateElement = div
  val vacantKindsArea = span
  val kenshinArea = span
  var boxesWrapper = div
  val ele = div(cls := "appoint-column", cls := op.code)(
    dateElement(cls := "date")(
      dateRep(date),
      vacantKindsArea,
      kenshinArea(cls := "kenshin-area"),
      div(cls := "date-label")(
        ClinicOperation.getLabel(op)
      )
    ),
    boxesWrapper
  )
  ele(cls := s"date-${date}")
  dateElement(oncontextmenu := (onContextMenu _))
  CustomEvents.appointTimeCreated.handle(ele, {
    case (g, t) => createAppointTimeBox(g, t).foreach(box => 
      insertBox(box)
      CustomEvents.appointTimePostCreated.trigger(ele, t, true)
    )
  })
  CustomEvents.appointTimePostDeleted.handle(ele, deleted => {
    boxes = SortedCompList.delete(boxes, deleted.appointTimeId)
  })

  def init: Future[Unit] =
    for (gen, appFull) <- listAppoints(date)
    yield
      appFull.foreach { case (appointTime, appoints) =>
        createAppointTimeBox(gen, appointTime).foreach(box => {
          insertBox(box)
          box.addAppoints(gen, appoints)
        })
      }
      adjustUI()

  private def adjustUI(): Unit =
    adjustVacantClass(boxes, vacantKindsArea, dateElement)
    markKenshin(boxes, kenshinArea)

  def totalAppoints: Int =
    boxes.foldLeft(0)((acc, b) => acc + b.numSlots)

  private def insertBox(appointTimeBox: AppointTimeBox): Unit =
    boxes = SortedCompList.insert(boxes, appointTimeBox, boxesWrapper)

  private def createAppointTimeBox(
      g: Int,
      appointTime: AppointTime
  ): Option[AppointTimeBox] =
    given SyncedComp[AppointTimeBox, AppointTime] =
      AppointTimeBox.createSyncedComp(() =>
        findVacantFollowers(boxes, appointTime)
      )
    SyncedComp.createSynced(g, appointTime)

  protected def composeContextMenu: List[(String, () => Unit)] =
    List.empty

  private def onContextMenu(event: MouseEvent): Unit =
    event.preventDefault()
    event.stopPropagation()
    val contextMenu = composeContextMenu
    if !contextMenu.isEmpty then ContextMenu(contextMenu).open(event)

object AppointColumn:
  def dateRep(date: LocalDate): String = Misc.formatAppointDate(date)

  def listAppoints(
      date: LocalDate
  ): Future[(Int, List[(AppointTime, List[Appoint])])] =
    Api.listAppointTimeFilled(date)

  def findVacantFollowers(
      boxes: List[AppointTimeBox],
      appointTime: AppointTime
  ): List[AppointTime] =
    val list = boxes
      .dropWhile(b => b.appointTime.appointTimeId != appointTime.appointTimeId)
    AppointTime
      .extractAdjacentRunEmbedded(list, _.appointTime)
      ._1
      .tail
      .takeWhile(a => a.numSlots == 0)
      .map(_.appointTime)

  def probeVacantKinds(boxes: List[AppointTimeBox]): List[AppointKind] =
    boxes
      .map(b => b.probeVacantKind())
      .filter(_ != None)
      .sequence[Option, String]
      .map(_.toSet)
      .getOrElse(Set.empty)
      .toList
      .map(AppointKind(_))
      .sortBy(_.ord)

  def adjustVacantClass(
      boxes: List[AppointTimeBox],
      vacantKindsArea: HTMLElement,
      dateElement: HTMLElement
  ): Unit =
    val kinds = probeVacantKinds(boxes)
    val wrapper = vacantKindsArea
    wrapper(clear)
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

  def countKenshin(boxes: List[AppointTimeBox]): Int =
    boxes.foldLeft(0)((acc, ele) => acc + ele.countKenshin())

  def markKenshin(boxes: List[AppointTimeBox], kenshinArea: HTMLElement): Unit =
    val n = countKenshin(boxes)
    kenshinArea(clear)
    if n > 0 then kenshinArea(s"健$n")



// case class AppointColumnOrig(
//     date: LocalDate,
//     op: ClinicOperation
// )(using EventFetcher):
//   var boxes: List[AppointTimeBox] = List.empty
//   val dateElement = div()
//   val vacantKindsArea = span()
//   val kenshinArea = span()
//   var boxWrapper = div()
//   val ele = div(cls := "appoint-column", cls := op.code)(
//     dateElement(cls := "date")(
//       dateRep,
//       vacantKindsArea,
//       kenshinArea(cls := "kenshin-area"),
//       div(cls := "date-label")(
//         ClinicOperation.getLabel(op)
//       ),
//       oncontextmenu := (onContextMenu _)
//     ),
//     boxWrapper
//   )

//   def updateUI(): Unit =
//     adjustVacantClass()
//     markKenshin()

//   ele.addCreatedListener[AppointTime](event => {
//     val created = event.dataAs[AppointTime]
//     if created.date == date then
//       addAppointTime(event.appEventId, created)
//       updateUI()
//   })
//   ele.addCreatedListener[Appoint](event =>
//     val created = event.dataAs[Appoint]
//     if hasAppointTimeId(created.appointTimeId) then updateUI()
//   )
//   ele.addUpdatedAllListener[Appoint](event =>
//     val updated = event.dataAs[Appoint]
//     if hasAppointTimeId(updated.appointTimeId) then updateUI()
//   )
//   ele.addDeletedAllListener[Appoint](event =>
//     val deleted = event.dataAs[Appoint]
//     if hasAppointTimeId(deleted.appointTimeId) then updateUI()
//   )

//   def dateRep: String = Misc.formatAppointDate(date)

//   def probeVacantKinds(): List[AppointKind] =
//     boxes
//       .map(b => b.probeVacantKind())
//       .filter(_ != None)
//       .sequence[Option, String]
//       .map(_.toSet)
//       .getOrElse(Set.empty)
//       .toList
//       .map(AppointKind(_))
//       .sortBy(_.ord)

//   def adjustVacantClass(): Unit =
//     val kinds = probeVacantKinds()
//     val wrapper = vacantKindsArea
//     wrapper(clear)
//     if kinds.isEmpty then dateElement(cls :- "vacant")
//     else
//       dateElement(cls := "vacant")
//       kinds.foreach(k => {
//         val icon =
//           Icons.circleFilled(
//             Icons.defaultStaticStyle,
//             css(style => {
//               style.fill = k.iconColor
//             })
//           )
//         wrapper(icon)
//       })

//   def countKenshin(): Int =
//     boxes.foldLeft(0)((acc, ele) => acc + ele.countKenshin())

//   def markKenshin(): Unit =
//     val n = countKenshin()
//     kenshinArea(clear)
//     if n > 0 then kenshinArea(s"健$n")

//   def totalAppoints: Int =
//     boxWrapper.qSelectorAllCount(".appoint-slot")

//   def hasAppointTimeId(appointTimeId: Int): Boolean =
//     boxes.find(_.appointTime.appointTimeId == appointTimeId).isDefined

//   def composeContextMenu(
//       prev: List[(String, () => Unit)]
//   ): List[(String, () => Unit)] =
//     prev

//   def onContextMenu(event: MouseEvent): Unit =
//     event.preventDefault()
//     val contextMenu = composeContextMenu(List.empty)
//     if !contextMenu.isEmpty then ContextMenu(contextMenu).open(event)

//   def findVacantFollowers(
//       appointTime: AppointTime
//   ): List[AppointTime] =
//     val list = boxes
//       .dropWhile(b => b.appointTime.appointTimeId != appointTime.appointTimeId)
//     AppointTime
//       .extractAdjacentRunEmbedded(list, _.appointTime)
//       ._1
//       .tail
//       .takeWhile(a => a.numSlots == 0)
//       .map(_.appointTime)

//   def makeAppointTimeBox(
//       appointTime: AppointTime,
//       gen: Int,
//       findVacantRegular: () => List[AppointTime]
//   ): AppointTimeBox =
//     new AppointTimeBox(gen, appointTime, () => findVacantFollowers(appointTime))

//   def setAppointTimes(
//       gen: Int,
//       appointTimesFilled: List[(AppointTime, List[Appoint])]
//   ): Unit =
//     boxes = appointTimesFilled.map(item =>
//       item match {
//         case (appointTime, appoints) =>
//           val box = makeAppointTimeBox(
//             appointTime,
//             gen,
//             () => findVacantFollowers(appointTime)
//           )
//           box.addAppoints(gen, appoints)
//           boxWrapper(box.ele)
//           box
//       }
//     )
//     updateUI()

//   def addAppointTime(gen: Int, appointTime: AppointTime): Unit =
//     val box = makeAppointTimeBox(
//       appointTime,
//       gen,
//       () => findVacantFollowers(appointTime)
//     )
//     boxWrapper(box.ele)
//     //boxes = Types.insert(box, _.ele, boxes, boxWrapper)
//     updateUI()

//   def removeAppointTime(appointTime: AppointTime, gen: Int): Unit =
//     ()
// // boxes = Types.delete(
// //   _.appointTimeId == appointTime.appointTimeId,
// //   _.ele,
// //   boxes,
// //   boxWrapper
// // )
