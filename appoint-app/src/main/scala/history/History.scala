package dev.myclinic.scala.web.appoint.history

import dev.myclinic.scala.model.{AppEvent, Appoint, AppointTime}
import scala.concurrent.Future
import dev.myclinic.scala.web.appoint.Misc
import concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.webclient.Api

trait History:
  def description(): Future[String]
  def resume: Option[() => Future[Either[String, Unit]]]

// case class AppointCanceled(appoint: Appoint)
//     extends History:
//   def description: String = {
//     val date = Misc.formatAppointDate(appointTime.date)
//     val time = Misc.formatAppointTime(appointTime.fromTime)
//     s"予約キャンセル：${appoint.patientName} ${date} ${time}"
//   }
//   def resume: Option[() => Future[Either[String, Unit]]] = None
