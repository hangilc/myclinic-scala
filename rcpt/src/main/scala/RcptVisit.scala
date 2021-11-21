package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.{VisitEx, MeisaiSection, MeisaiSectionItem}
import dev.myclinic.java.{HoukatsuKensa, HokenUtil}
import java.time.LocalDate

object RcptVisit:
  def getMeisai(visit: VisitEx)(using
      houkatsuKensa: HoukatsuKensa
  ): List[(MeisaiSection, List[MeisaiSectionItem])] =
    if !visit.drugs.isEmpty then
      new RuntimeException("visit drug is not supported")
    var units: List[MeisaiUnit] = List.empty
    visit.shinryouList.foreach(s => {
      val u = MeisaiUnit.fromShinryou(s, visit.visitedAt.toLocalDate)
      units = add(u, units)
    })
    visit.conducts.foreach(c => {
      val u = MeisaiUnit.fromConduct(c)
      units = add(u, units)
    })
    units
      .groupBy(_.section)
      .toList
      .sortBy(_._1.ordinal)
      .map({ case (sect, units) =>
        (sect, units.map(_.toItem))
      })

  private def add(unit: MeisaiUnit, repo: List[MeisaiUnit]): List[MeisaiUnit] =
    repo match {
      case Nil => List(unit)
      case h :: t =>
        h.merge(unit) match {
          case Some(merged) => merged :: t
          case None         => h :: add(unit, t)
        }
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
          update(util.kouhiFutanWari(kouhi.futansha)))
        futanWari
      }
    }
