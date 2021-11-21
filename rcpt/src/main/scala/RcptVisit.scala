package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.{
  VisitEx,
  MeisaiSection,
  MeisaiSectionItem,
  Meisai
}
import dev.myclinic.java.{HoukatsuKensa, HokenUtil, RcptCalc}
import java.time.LocalDate

object RcptVisit:
  def getMeisai(visit: VisitEx)(using
      houkatsuKensa: HoukatsuKensa
  ): Meisai =
    if !visit.drugs.isEmpty then
      new RuntimeException("visit drug is not supported")
    given RcptCalc = new RcptCalc()
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
        (sect, units.map(_.toItem))
      })
    val futanWari: Int = calcFutanWari(visit)
    val calc: RcptCalc = new RcptCalc()
    val totalTen: Int = Meisai.calcTotalTen(items)
    Meisai(items, futanWari, calc.calcCharge(totalTen, futanWari))

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

  def calcFutanWari(visit: VisitEx): Int =
    val util = new HokenUtil()
    visit.toVisit.futanWariOverride match {
      case Some(futanWari) => futanWari
      case None => {
        var futanWari = 10
        def update(value: Int): Unit =
          futanWari = Math.min(futanWari, value)
        visit.shahokokuho.foreach(shahokokuho => {
          val birthday: LocalDate = visit.patient.birthday
          val rcptAge = util.calcRcptAge(
            birthday.getYear,
            birthday.getMonthValue,
            birthday.getDayOfMonth,
            visit.visitedAt.getYear,
            visit.visitedAt.getMonthValue
          )
          update(util.calcShahokokuhoFutanWariByAge(rcptAge))
          if shahokokuho.kourei > 0 then update(shahokokuho.kourei)
        })
        visit.roujin.foreach(roujin => update(roujin.futanWari))
        visit.koukikourei.foreach(koukikourei => update(koukikourei.futanWari))
        visit.kouhiList.foreach(kouhi =>
          update(util.kouhiFutanWari(kouhi.futansha))
        )
        futanWari
      }
    }
