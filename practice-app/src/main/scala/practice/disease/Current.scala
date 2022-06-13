package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil

case class Current(list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]):
  val ele = div(
    cls := "practice-disease-current",
    list.map(CurrentItem(_).ele)
  )

case class CurrentItem(diseaseInfo: (Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])):
  val (disease, bMaster, adjList) = diseaseInfo
  val ele = div(
    span(DiseaseUtil.diseaseNameOf(diseaseInfo), cls := "practice-disease-current-item-name"),
    span(DateUtil.formatDate(disease.startDate), cls := "practice-disease-current-item-date")
  )