package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.*
import dev.myclinic.scala.util.HokenRep
import java.time.LocalDate
import math.Ordered.orderingToOrdered

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

  def hokenRep(hoken: Hoken): String =
    val info = hoken match {
      case h: Shahokokuho => HokenInfo(shahokokuho = Some(h))
      case h: Roujin      => HokenInfo(roujin = Some(h))
      case h: Koukikourei => HokenInfo(koukikourei = Some(h))
      case h: Kouhi       => HokenInfo(kouhiList = List(h))
    }
    hokenRep(info)

  def validFromOf(hoken: Hoken): LocalDate =
    hoken match {
      case h: Shahokokuho => h.validFrom
      case h: Koukikourei => h.validFrom
      case h: Kouhi       => h.validFrom
      case h: Roujin      => h.validFrom
    }

  def toHokenList(
      shahokokuhoList: List[Shahokokuho],
      koukikoureiList: List[Koukikourei],
      roujinList: List[Roujin],
      kouhiList: List[Kouhi]
  ): List[Hoken] =
    List.empty[
      Hoken
    ] ++ shahokokuhoList ++ koukikoureiList ++ roujinList ++ kouhiList

  def suggestKoukikoureiValidUpto(validFrom: LocalDate): LocalDate =
    if validFrom >= LocalDate.of(2022, 8, 1) && validFrom <= LocalDate.of(
        2022,
        9,
        30
      )
    then LocalDate.of(2022, 9, 30)
    else
      val a = LocalDate.of(validFrom.getYear, 7, 31)
      if validFrom <= a then a
      else a.plusYears(1)

  object Ext:
    extension (s: Shahokokuho)
      def rep: String = HokenRep.shahokokuhoRep(
        s.hokenshaBangou,
        s.koureiFutanWari
      )

    extension (k: Koukikourei)
      def rep: String = HokenRep.koukikoureiRep(
        k.futanWari
      )

    extension (r: Roujin) def rep: String = HokenRep.roujinRep(r.futanWari)

    extension (k: Kouhi) def rep: String = HokenRep.kouhiRep(k.futansha)
