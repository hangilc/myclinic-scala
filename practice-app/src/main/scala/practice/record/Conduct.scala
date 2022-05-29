package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.model.ConductEx
import dev.myclinic.scala.apputil.ConductUtil

class Conduct(c: ConductEx):
  val ele = div(innerText := rep(c))

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

object Conduct:
  given Comp[Conduct] = _.ele
  given Dispose[Conduct] = _ => ()
