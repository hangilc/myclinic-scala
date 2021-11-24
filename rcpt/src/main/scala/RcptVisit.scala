package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.{
  VisitEx,
  MeisaiSection,
  MeisaiSectionItem,
  MeisaiSectionData,
  Meisai
}
import dev.myclinic.java.{HoukatsuKensa}
import dev.myclinic.scala.util.RcptUtil
import dev.myclinic.scala.apputil.FutanWari
import java.time.LocalDate

object RcptVisit:
  def getMeisai(visit: VisitEx)(using
      houkatsuKensa: HoukatsuKensa
  ): Meisai =
    if !visit.drugs.isEmpty then
      new RuntimeException("visit drug is not supported")
    var units: List[MeisaiUnit] = List.empty
    visit.shinryouList.foreach(s => {
      val u = MeisaiUnit.fromShinryou(s, visit.visitedAt.toLocalDate)
      units = add(u, units)
    })
    visit.conducts.foreach(c => {
      MeisaiUnit.fromConduct(c).foreach(u => { units = add(u, units) })
    })
    val items = units
      .groupBy(_.section)
      .toList
      .sortBy(_._1.ordinal)
      .map({ case (sect, units) =>
        MeisaiSectionData(sect, units.map(_.toItem))
      })
    val futanWari: Int = FutanWari.calcFutanWari(visit)
    val totalTen: Int = Meisai.calcTotalTen(items)
    Meisai(items, futanWari, RcptUtil.calcCharge(totalTen, futanWari))

  private def add(unit: MeisaiUnit, repo: List[MeisaiUnit]): List[MeisaiUnit] =
    val section: MeisaiSection = unit.section
    repo match {
      case Nil => List(unit)
      case h :: t =>
        if h.section == section then
          h.merge(unit) match {
            case Some(merged) => merged :: t
            case None         => h :: add(unit, t)
          }
        else h :: add(unit, t)
    }
