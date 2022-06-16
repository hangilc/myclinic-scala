package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil

case class Edit(
    list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])],
    onDone: Edit => Unit
):
  import Edit.{Item, Disp}
  val selection = Selection[Item](list.map(Item.apply.tupled(_)), _.ele)
  val ele = div(
    list.map(Item.apply.tupled(_).ele)
  )

object Edit:
  case class Item(
    disease: Disease,
    byoumeiMaster: ByoumeiMaster,
    adjList: List[(DiseaseAdj, ShuushokugoMaster)]
  ):
    val ele = div(
      DiseaseUtil.diseaseNameOf(byoumeiMaster, adjList.map(_._2))
    )

  case class Disp(
    var disease: Disease,
    var byoumeiMaster: ByoumeiMaster,
    var adjList: List[(DiseaseAdj, ShuushokugoMaster)]
  ):
    val nameEle = span
    val startDateEle = span
    val endReasonEle = span
    val endDateEle = span
    val ele = div(
      cls := "practice-disease-edit-disp",
      div(
        div("名称："),
        nameEle
      ),
      div(
        div("開始日："),
        startDateEle
      ),
      div(
        div("転機："),
        endReasonEle
      ),
      div(
        div("終了日："),
        endDateEle
      ),
    )
