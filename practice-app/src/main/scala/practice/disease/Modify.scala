package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.dateinput.EditableDate

case class Modify(
    disease: Disease,
    byoumeiMaster: ByoumeiMaster,
    adjList: List[(DiseaseAdj, ShuushokugoMaster)]
):
  val nameSpan = span
  val startDateEdit = EditableDate(disease.startDate, "開始日")
  val ele = div(
    div("名前：", nameSpan),
    div(startDateEdit.ele),
    div("から")
  )
