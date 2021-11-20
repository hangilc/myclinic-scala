package dev.myclinic.scala.rcpt

import dev.myclinic.scala.model.{VisitEx, MeisaiSection, MeisaiSectionItem}
import dev.myclinic.java.HoukatsuKensa
import scala.collection.mutable.ListBuffer

object RcptVisit:
  def getMeisai(visit: VisitEx)(using
      houkatsuKensa: HoukatsuKensa
  ): List[(MeisaiSection, List[MeisaiSectionItem])] =
    val items: ListBuffer[MeisaiUnit] = ListBuffer()
    if !visit.drugs.isEmpty then new RuntimeException("visit drug is not supported")
    visit.shinryouList.foreach(s => items :+ MeisaiUnit.fromShinryou(s))
    visit.conducts.foreach(c => items :+ MeisaiUnit.fromConduct(c))
    items.toList.groupBy(_.section).toList.sortBy(_._1.ordinal)
    .map({
      case (sect, units) => (sect, units.map(_.toItem))
    })