package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.util.HokenRep
import dev.myclinic.scala.model.HokenInfo

object HokenUtil:
  def hokenRep(visit: VisitEx): String =
    hokenRep(visit.hoken)

  def hokenRep(hoken: HokenInfo): String =
    HokenRep(
      hoken.shahokokuho.map(_.hokenshaBangou),
      hoken.shahokokuho.flatMap(_.koureiFutanWari),
      hoken.koukikourei.map(_.futanWari),
      hoken.roujin.map(_.futanWari),
      hoken.kouhiList.lift(0).map(_.futansha),
      hoken.kouhiList.lift(1).map(_.futansha),
      hoken.kouhiList.lift(2).map(_.futansha)
    )
