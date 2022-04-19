package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}

class Record(visitId: Int):
  val ele = div(visitId.toString)
  def init(visitEx: VisitEx): Unit =
    ()

