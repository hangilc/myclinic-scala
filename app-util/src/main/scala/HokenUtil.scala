package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.*
import dev.myclinic.scala.util.HokenRep

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

  object Ext:
    extension (s: Shahokokuho)
      def rep: String = HokenRep.shahokokuhoRep(
        s.hokenshaBangou, s.koureiFutanWari
      )

    extension (k: Koukikourei)
      def rep: String = HokenRep.koukikoureiRep(
        k.futanWari
      )

    extension (r: Roujin)
      def rep: String = HokenRep.roujinRep(r.futanWari)

    extension (k: Kouhi)
      def rep: String = HokenRep.kouhiRep(k.futansha)
      
