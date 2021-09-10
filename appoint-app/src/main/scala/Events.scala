package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.model._
import dev.myclinic.scala.webclient.Api

object Events {

  sealed trait ModelEvent
  case class AppointCreated(appoint: Appoint) extends ModelEvent
  case class AppointUpdated(appoint: Appoint) extends ModelEvent
  case class AppointDeleted(appoint: Appoint) extends ModelEvent
  case class Unknown(orig: AppEvent) extends ModelEvent

  def convert(appEvent: AppEvent): ModelEvent = appEvent match {
    case AppEvent(_, _, _, "appoint", kind, encodedData) => {
      val data = Api.fromJson[Appoint](encodedData)
      kind match {
        case "created" => AppointCreated(data)
        case "updated" => AppointUpdated(data)
        case "deleted" => AppointDeleted(data)
      }
    }
    case _ => Unknown(appEvent)
  }
}