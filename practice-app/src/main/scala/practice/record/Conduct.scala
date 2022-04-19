package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.model.ConductEx
import dev.myclinic.scala.apputil.ConductUtil

class Conduct(visit: VisitEx):
  val ele = div()

  visit.conducts.foreach(conduct => {
    ele(div(innerText := rep(conduct)))
  })

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
