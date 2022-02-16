package dev.myclinic.scala.web.appoint

import dev.fujiwara.domq.{CustomEventConnect, CustomEvent}
import dev.myclinic.scala.model.*

object CustomEvents:
  private val prefix: String = "appoint-app"
  val appointTimeCreated =
    CustomEventConnect[(Int, AppointTime)](s"${prefix}-appoiont-time-created")
  val appointTimePostCreated =
    CustomEventConnect[AppointTime](s"${prefix}-appoiont-time-post-created")
  val appointTimePostUpdated =
    CustomEventConnect[AppointTime](s"${prefix}-appoiont-time-post-updated")
  val appointTimePostDeleted =
    CustomEventConnect[AppointTime](s"${prefix}-appoiont-time-post-deleted")

  val appointCreated =
    CustomEventConnect[(Int, Appoint)](s"${prefix}-appoiont-created")
  val appointPostCreated =
    CustomEventConnect[Appoint](s"${prefix}-appoiont-post-created")
  val appointPostUpdated =
    CustomEventConnect[Appoint](s"${prefix}-appoiont-post-updated")
  val appointPostDeleted =
    CustomEventConnect[Appoint](s"${prefix}-appoiont-post-deleted")
