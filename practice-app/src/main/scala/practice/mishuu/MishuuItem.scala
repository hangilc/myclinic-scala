package dev.myclinic.scala.web.practiceapp.practice.mishuu

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.TypeClasses.DataProvider
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate

case class MishuuItem(visit: Visit, meisai: Meisai):
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
  given DataProvider[MishuuItem, (Visit, Meisai)] with
    def getData(c: MishuuItem): (Visit, Meisai) = 
      (c.visit, c.meisai)
  given Ordering[(Visit, Meisai)] = Ordering.by[(Visit, Meisai), Int](_._1.visitId)