package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.myclinic.scala.model.*
import dev.myclinic.scala.apputil.ConductUtil

object ConductHelper:
  def rep(c: ConductEx): String =
    (List(
      s"[${c.kind.rep}]"
    ) ++
      (
        c.gazouLabel.fold(List.empty)(List(_))
      ) ++
      c.shinryouList.map(s => "* " + s.master.name) ++
      c.drugs.map(d => "* " + ConductUtil.conductDrugRep(d)) ++
      c.kizaiList.map(k => "* " + ConductUtil.conductKizaiRep(k))
      ).mkString("\n")

