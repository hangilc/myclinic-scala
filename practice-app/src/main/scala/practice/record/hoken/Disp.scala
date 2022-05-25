package dev.myclinic.scala.web.practiceapp.practice.record.hoken

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.LocalEventPublisher
import dev.myclinic.scala.model.*
import dev.myclinic.scala.apputil.HokenUtil

class Disp(hoken: HokenInfo):
  val onClick = LocalEventPublisher[Unit]

  val ele = div(cls := "practice-hoken-disp",
    HokenUtil.hokenRep(hoken),
    onclick := (_ => onClick.publish(()))
  )
