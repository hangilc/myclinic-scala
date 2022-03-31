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
import dev.myclinic.scala.web.appbase.EventFetcher
import java.time.LocalDate
import dev.myclinic.scala.clinicop.ClinicOperation

class AdminAppointSheet(using EventFetcher) extends AppointSheet:
  import AdminAppointSheet.*
  val cog = Icons.cog(Icons.defaultStyle)
  topMenu.topMenuBox(cog)
  cog(onclick := onCogClick)

  def onCogClick(event: MouseEvent): Unit =
    ContextMenu(
      List("予約枠わりあて" -> (() => doFillAppointTimes.tupled(topMenu.getDateRange)))
    ).open(event)

  override def makeAppointColumn(date: LocalDate, op: ClinicOperation): AppointColumn =
    new AdminAppointColumn(date, op)

object AdminAppointSheet:
  def doFillAppointTimes(from: LocalDate, upto: LocalDate): Unit =
    println("fill-appoint-times")
    Api.fillAppointTimes(from, upto)

//class AdminAppointSheet(using EventFetcher) extends AppointSheet:
// val cog = Icons.cog(Icons.defaultStyle)
// TopMenu.topMenuBox(cog)
// cog(onclick := onCogClick)

// override def makeAppointColumn(date: LocalDate, op: ClinicOperation): AppointColumn =
//   new AdminAppointColumn(date, op)

// def onCogClick(event: MouseEvent): Unit =
//   ContextMenu(List("予約枠わりあて" -> doFillAppointTimes)).open(event)

// def doFillAppointTimes(): Unit =
//   println(("date-range", dateRange))
//   dateRange.map {
//     case (from, upto) => Api.fillAppointTimes(from, upto)
//   }
