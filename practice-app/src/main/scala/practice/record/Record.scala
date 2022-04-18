package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}

class Record(visitEx: VisitEx):
  val ele = div(visitEx.visitId.toString)

