package dev.myclinic.scala.web.practiceapp.practice.mishuu

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.TypeClasses.DataProvider
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate

case class MishuuItem(visit: Visit, patient: Patient, meisai: Meisai):
  val ele = div(
    rep
  )

  def rep: String =
    val d = KanjiDate.dateToKanji(visit.visitedAt.toLocalDate)
    val c = meisai.charge
    s"${d} ${c}円"

object MishuuItem:
  given Comp[MishuuItem] = _.ele
  given Dispose[MishuuItem] = _ => ()
  given DataProvider[MishuuItem, (Visit, Patient, Meisai)] with
    def getData(c: MishuuItem): (Visit, Patient, Meisai) =
      (c.visit, c.patient, c.meisai)
  given Ordering[(Visit, Patient, Meisai)] =
    Ordering.by[(Visit, Patient, Meisai), Int](_._1.visitId)
