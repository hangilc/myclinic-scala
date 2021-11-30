package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.util.HokenRep

object HokenUtil:
  def hokenRep(visit: VisitEx): String =
    HokenRep(
      visit.shahokokuho.map(_.hokenshaBangou),
      visit.shahokokuho.flatMap(_.koureiFutanWari),
      visit.koukikourei.map(_.futanWari),
      visit.roujin.map(_.futanWari),
      visit.kouhiList.lift(0).map(_.futansha),
      visit.kouhiList.lift(1).map(_.futansha),
      visit.kouhiList.lift(2).map(_.futansha)
    )
