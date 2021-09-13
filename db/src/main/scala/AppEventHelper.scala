package dev.myclinic.scala.db

import dev.myclinic.scala.model._
import doobie._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.modeljson.Implicits.{given}

object AppEventHelper {

  def enterAppointEvent(eventId: Int, kind: String, app: Appoint): ConnectionIO[AppEvent] = {
    val data = app.asJson.toString()
    DbEventPrim.enterAppEvent(eventId, "appoint", kind, data)
  }

}
