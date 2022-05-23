package dev.myclinic.scala.web.practiceapp.practice.record.hoken

import dev.myclinic.scala.model.HokenInfo
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.apputil.HokenUtil.Ext.*

object Edit:
  def open(shahoOpt: Option[Shahokokuho], koukikoureiOpt: Option[Koukikourei], kouhiList: List[Kouhi]): Unit =
    shahoOpt.get.rep
