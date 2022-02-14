package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.{CustomEventConnect, CustomEvent}
import dev.myclinic.scala.model.*

object CustomEvents:
  val appointTimeCreated =
    CustomEventConnect[(Int, AppointTime)]("appoint-app-appoiont-time-created")
