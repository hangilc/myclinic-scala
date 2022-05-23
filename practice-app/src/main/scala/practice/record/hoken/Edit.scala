package dev.myclinic.scala.web.practiceapp.practice.record.hoken

import dev.myclinic.scala.model.HokenInfo
import dev.fujiwara.domq.all.{*, given}

class Edit(hoken: HokenInfo):
  val ele = div("EDIT")
