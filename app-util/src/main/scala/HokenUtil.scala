package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.util.HokenRep

object HokenUtil:
  def hokenRep(visit: VisitEx): String =
    HokenRep(
      visit.shahokokuho.map(_.hokenshaBangou),
      visit.shahokokuho.flatMap(h =>
        if h.kourei > 0 then Some(h.kourei) else None
      ),
      visit.koukikourei.map(_.futanWari),
      visit.roujin.map(_.futanWari),
      visit.kouhiList.lift(0).map(_.futansha),
      visit.kouhiList.lift(1).map(_.futansha),
      visit.kouhiList.lift(2).map(_.futansha)
    )
