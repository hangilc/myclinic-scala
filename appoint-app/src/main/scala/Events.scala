package dev.myclinic.scala.web.appoint

import dev.myclinic.scala.model._
import dev.myclinic.scala.webclient.Api

object EventDispatcher {
  import Events._
  def dispatch(event: Events.ModelEvent): Unit = event match {
    case AppointCreated(_) | AppointUpdated(_) | AppointDeleted(_) => 
      AppointSheet.handleEvent(event)
    case _ =>
  }
}

object Events {

  sealed trait ModelEvent
  case class AppointCreated(appoint: Appoint) extends ModelEvent
  case class AppointUpdated(appoint: Appoint) extends ModelEvent
  case class AppointDeleted(appoint: Appoint) extends ModelEvent

  def handle(appEvent: AppEvent): Unit = appEvent match {
    case AppEvent(_, _, _, "appoint", kind, encodedData) => {
      val data = Api.fromJson[Appoint](encodedData)
      kind match {
        case "created" => EventDispatcher.dispatch(AppointCreated(data))
        case "updated" => EventDispatcher.dispatch(AppointUpdated(data))
        case "deleted" => EventDispatcher.dispatch(AppointDeleted(data))
      }
    }
    case _ =>
  }
}