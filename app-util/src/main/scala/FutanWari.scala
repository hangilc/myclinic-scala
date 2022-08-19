package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.util.RcptUtil
import java.time.LocalDate

object FutanWari:
  def calcFutanWari(visit: VisitEx): Int =
    Should be fixed!
    visit.toVisit.futanWariOverride match {
      case Some(futanWari) => futanWari
      case None => {
        var futanWari = 10
        def update(value: Int): Unit =
          futanWari = Math.min(futanWari, value)
        visit.shahokokuho.foreach(shahokokuho => {
          val birthday: LocalDate = visit.patient.birthday
          val rcptAge =
            RcptUtil.calcRcptAge(birthday, visit.visitedAt.toLocalDate)
          update(calcShahokokuhoFutanWariByAge(rcptAge))
          shahokokuho.koureiFutanWari.foreach(update(_))
        })
        visit.roujin.foreach(roujin => update(roujin.futanWari))
        visit.koukikourei.foreach(koukikourei => update(koukikourei.futanWari))
        visit.kouhiList.foreach(kouhi =>
          update(kouhiFutanWari(kouhi.futansha))
        )
        futanWari
      }
    }

  def calcShahokokuhoFutanWariByAge(age: Int): Int =
    if (age < 3) then 2
    else if (age >= 70) then 2
    else 3

  def kouhiFutanWari(futanshaBangou: Int): Int =
    if (futanshaBangou / 1000000 == 41) then 1
    else if ((futanshaBangou / 1000) == 80136) then 1
    else if ((futanshaBangou / 1000) == 80137) then 0
    else if ((futanshaBangou / 1000) == 81136) then 1
    else if ((futanshaBangou / 1000) == 81137) then 0
    else if ((futanshaBangou / 1000000) == 88) then 0
    else {
      System.err.println("unknown kouhi futansha: " + futanshaBangou)
      0
    }
