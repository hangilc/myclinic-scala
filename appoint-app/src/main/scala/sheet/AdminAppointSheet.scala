package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{Appoint, AppointTime}
import dev.fujiwara.domq.Icons
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.ContextMenu
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.web.appbase.EventPublishers
import java.time.LocalDate

class AdminAppointSheet(using EventPublishers) extends AppointSheet:
  val cog = Icons.cog(Icons.defaultStyle)
  TopMenu.topMenuBox(cog)
  cog(onclick := onCogClick)

  override def makeAppointTimeBox(
      appointTime: AppointTime,
      followingVacantRegular: () => Option[AppointTime]
  ): AppointTimeBox =
    AdminAppointTimeBox(appointTime, followingVacantRegular)

  def onCogClick(event: MouseEvent): Unit =
    ContextMenu(List("予約枠わりあて" -> doFillAppointTimes)).open(event)

  def doFillAppointTimes(): Unit =
    dateRange.map {
      case (from, upto) => Api.fillAppointTimes(from, upto)
    }

  override def modifyColumn(col: AppointColumn): AppointColumn =
    val modified = super.modifyColumn(col)
    modified.dateElement(oncontextmenu := (event => onDateContextMenu(event, modified.date)))
    modified

  def onDateContextMenu(event: MouseEvent, date: LocalDate): Unit =
    ContextMenu(List(
      "予約枠追加" -> (() => doAddAppointTime(date))
    )).open(event)

  def doAddAppointTime(date: LocalDate): Unit =
    AddAppointTimeDialog(date).open()
    
