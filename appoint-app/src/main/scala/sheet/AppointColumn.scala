package dev.myclinic.scala.web.appoint.sheet

import java.time.{LocalTime, LocalDate}
import dev.myclinic.scala.model.{*, given}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.web.appoint
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, MouseEvent}
import cats.syntax.all.*
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.clinicop.*
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import scala.math.Ordered.orderingToOrdered
import dev.myclinic.scala.web.appoint.CustomEvents
import dev.myclinic.scala.web.appbase.SyncedDataSource

case class AppointColumn(date: LocalDate, op: ClinicOperation)(using
    EventFetcher
):
  import AppointColumn.*
  var boxesWrapper = div
  val boxes: CompSortList[AppointTimeBox] = new CompSortList[AppointTimeBox](boxesWrapper)
  val dateElement = div
  val vacantKindsArea = span
  val kenshinArea = span
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
  CustomEvents.appointTimeCreated.handle(
    ele,
    { case (g, t) =>
      val box = createAppointTimeBox(g, t)
      insertBox(box)
      adjustUI()
      CustomEvents.appointTimePostCreated.trigger(ele, t, true)
    }
  )
  CustomEvents.appointTimePostUpdated.handle(ele, _ => adjustUI())
  CustomEvents.appointTimePostDeleted.handle(
    ele,
    deleted => {
      boxes.remove(c => c.appointTime.appointTimeId == deleted.appointTimeId)
      adjustUI()
    }
  )
  CustomEvents.appointPostCreated.handle(ele, _ => adjustUI())
  CustomEvents.appointPostUpdated.handle(ele, _ => adjustUI())
  CustomEvents.appointPostDeleted.handle(ele, _ => adjustUI())

  def init: Future[Unit] =
    for (gen, appFull) <- listAppoints(date)
    yield
      appFull.foreach { case (appointTime, appoints) =>
        val box = createAppointTimeBox(gen, appointTime)
        insertBox(box)
        box.addAppoints(gen, appoints)
      }
      adjustUI()

  private def adjustUI(): Unit =
    adjustVacantClass(boxes.list, vacantKindsArea, dateElement)
    markKenshin(boxes.list, kenshinArea)

  def totalAppoints: Int =
    boxes.list.foldLeft(0)((acc, b) => acc + b.numSlots)

  private def insertBox(appointTimeBox: AppointTimeBox): Unit =
    boxes += appointTimeBox

  protected def createAppointTimeBox(
      g: Int,
      appointTime: AppointTime
  ): AppointTimeBox =
    AppointTimeBox(
      SyncedDataSource(g, appointTime),
      () => findVacantFollowers(boxes.list, appointTime)
    )

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
