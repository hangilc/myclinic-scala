package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.web.practiceapp.practice.record.hoken.{Disp, Edit}
import dev.myclinic.scala.model.HokenInfo

class Hoken(hoken: HokenInfo):
  val ele = div
  disp

  def disp: Unit =
    val d = Disp(hoken)
    d.onClick.subscribe(_ => edit)
    ele(d.ele)

  def edit: Unit =
    val e = Edit(hoken)
    ele(clear, e.ele)
