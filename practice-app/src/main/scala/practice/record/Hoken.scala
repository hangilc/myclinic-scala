package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.model.VisitEx

class Hoken(visit: VisitEx):
  val ele = div(
    HokenUtil.hokenRep(visit)
  )