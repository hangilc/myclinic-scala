package dev.myclinic.scala.db

import cats.*
import cats.implicits.*
import cats.effect.IO
import dev.myclinic.scala.model.*
import dev.myclinic.scala.db.{DbGazouLabelPrim => Prim}
import doobie.*
import doobie.implicits.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

trait DbGazouLabel extends Mysql:
  def setGazouLabel(conductId: Int, label: String): IO[AppEvent] =

    val op = 
      for
        glOpt <- Prim.getGazouLabel(conductId).option
        event <- glOpt match {
          case Some(gl) => Prim.updateGazouLabel(GazouLabel(conductId, label))
          case None => Prim.enterGazouLabel(GazouLabel(conductId, label))
        }
      yield event
    mysql(op)
