package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.model._
import dev.myclinic.scala.webclient.Api

object Events {

  class ModelEvent(val eventId: Int)
  case class AppointCreated(eventId: Int, appoint: Appoint) extends ModelEvent(eventId)
  case class AppointUpdated(eventId: Int, appoint: Appoint) extends ModelEvent(eventId)
  case class AppointDeleted(eventId: Int, appoint: Appoint) extends ModelEvent(eventId)
  case class Unknown(eventId: Int, orig: AppEvent) extends ModelEvent(eventId)

  def convert(appEvent: AppEvent): ModelEvent = appEvent match {
    case AppEvent(_, eventId, _, "appoint", kind, encodedData) => {
      val data = Api.fromJson[Appoint](encodedData)
      kind match {
        case "created" => AppointCreated(eventId, data)
        case "updated" => AppointUpdated(eventId, data)
        case "deleted" => AppointDeleted(eventId, data)
      }
    }
    case _ => Unknown(appEvent.eventId, appEvent)
  }
}