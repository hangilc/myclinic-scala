package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*

case class Current(list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]):
  val ele = div(
    list.map(CurrentItem(_).ele)
  )

case class CurrentItem(diseaseInfo: (Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])):
  val ele = div(
    diseaseInfo._2.name
  )