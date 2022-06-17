package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.SelectProxy
import dev.fujiwara.dateinput.EditableDate
import dev.fujiwara.dateinput.EditableOptionalDate

case class Modify(
    disease: Disease,
    byoumeiMaster: ByoumeiMaster,
    adjList: List[(DiseaseAdj, ShuushokugoMaster)],
    examples: List[DiseaseExample]
):
  val nameSpan = span
  val startDateEdit = EditableDate(disease.startDate, title = "開始日")
  val endDateEdit = EditableOptionalDate(
    disease.endDate.value,
    title = "終了日",
    nullFormatter = () => "未終了"
  )
  val endReasonSelect = SelectProxy[DiseaseEndReason](
    DiseaseEndReason.values.toList,
    (opt, reason) => opt(reason.label)
  )
  val search = Search(() => startDateEdit.date, examples)
  val ele = div(
    div("名前：", nameSpan),
    div(startDateEdit.ele),
    div("から"),
    div(endDateEdit.ele),
    endReasonSelect.ele,
    div(
      button("入力", onclick := (doEnter _)),
      a("の疑い"),
      a("修飾語削除"),
      a("終了日クリア"),
      a("削除")
    ),
    search.ele
  )

  def doEnter(): Unit =
    ???
