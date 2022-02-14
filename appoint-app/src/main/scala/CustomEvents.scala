package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.{CustomEventConnect, CustomEvent}
import dev.myclinic.scala.model.*

object CustomEvents:
  val appointTimeCreated =
    CustomEventConnect[(Int, AppointTime)]("appoint-app-appoiont-time-created")
  val appointTimePostCreated =
    CustomEventConnect[AppointTime]("appoint-app-appoiont-time-post-created")
  val appointTimePostDeleted =
    CustomEventConnect[AppointTime]("appoint-app-appoiont-time-post-deleted")
  val appointPostDeleted =
    CustomEventConnect[Appoint]("appoint-app-appoint-post-deleted")
