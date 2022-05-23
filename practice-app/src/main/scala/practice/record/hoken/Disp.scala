package dev.myclinic.scala.web.practiceapp.practice.record.hoken

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.apputil.HokenUtil

class Disp(visit: VisitEx):
  val ele = div(
    HokenUtil.hokenRep(visit)
  )
