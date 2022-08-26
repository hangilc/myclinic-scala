package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import scala.language.implicitConversions

case class Current(
    list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])],
    modify: (Disease, ByoumeiMaster, List[ShuushokugoMaster]) => Unit
):
  val ele = div(
    cls := "practice-disease-current",
    list.map(CurrentItem(_, modify).ele)
  )

case class CurrentItem(
    diseaseInfo: (
        Disease,
        ByoumeiMaster,
        List[(DiseaseAdj, ShuushokugoMaster)]
    ),
    modify: (Disease, ByoumeiMaster, List[ShuushokugoMaster]) => Unit
):
  val (disease, bMaster, adjList) = diseaseInfo
  val ele = div(
    cls := "domq-cursor-pointer",
    span(
      DiseaseUtil.diseaseNameOf(diseaseInfo),
      cls := "practice-disease-current-item-name"
    ),
    span(
      DateUtil.formatDate(disease.startDate),
      cls := "practice-disease-current-item-date"
    ),
    onclick := (() => modify(disease, bMaster, adjList.map(_._2)))
  )
