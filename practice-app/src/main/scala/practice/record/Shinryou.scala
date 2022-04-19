package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.model.VisitEx

class Shinryou(visit: VisitEx):
  val ele = div(
    children := visit.shinryouList.map(s => div(s.master.name))
  )

