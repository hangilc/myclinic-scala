package dev.myclinic.scala.db

import dev.myclinic.scala.model._
import doobie._

object AppEventHelper {

  def enterAppointEvent(eventId: Int, kind: String, app: Appoint)(implicit
      encoder: JsonEncoder
  ): ConnectionIO[AppEvent] = {
    val data = encoder.toJson(app)
    DbEventPrim.enterAppEvent(eventId, "appoint", kind, data)
  }

}
