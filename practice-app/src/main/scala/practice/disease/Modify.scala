package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.SelectProxy
import dev.fujiwara.dateinput.EditableDate
import dev.fujiwara.dateinput.EditableOptionalDate
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import dev.myclinic.scala.webclient.{Api, global}

case class Modify(
    var disease: Disease,
    var byoumeiMaster: ByoumeiMaster,
    var shuushokugoMasters: List[ShuushokugoMaster],
    examples: List[DiseaseExample],
    onDone: () => Unit
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
  search.onByoumeiSelected(doByoumeiSelected _)
  search.onShuushokugoSelected(doShuushokugoSelected _)

  val ele = div(
    div("名前：", nameSpan),
    div(startDateEdit.ele),
    div("から"),
    div(endDateEdit.ele),
    endReasonSelect.ele,
    div(
      button("入力", onclick := (doEnter _)),
      a("の疑い", onclick := (doSusp _)),
      a("修飾語削除", onclick := (doDelAdj _)),
      a("終了日クリア", onclick := (doClearEndDate _)),
      a("削除", onclick := (doDelete _))
    ),
    search.ele
  )
  updateNameUI()

  private def doSusp(): Unit =
    for m <- Api.resolveShuushokugoMasterByName("の疑い", disease.startDate)
    yield m.foreach(doShuushokugoSelected _)

  private def doDelAdj(): Unit =
    shuushokugoMasters = List.empty
    updateNameUI()

  private def doClearEndDate(): Unit =
    disease = disease.copy(endDate = ValidUpto(None))
    updateNameUI()

  private def doDelete(): Unit =
    ShowMessage.confirm("この病名を削除していいですか？") { () =>
      for _ <- Api.deleteDiseaseEx(disease.diseaseId)
      yield onDone()
    }

  private def doEnter(): Unit =
    for
      _ <- Api.updateDiseaseEx(
        disease,
        shuushokugoMasters.map(_.shuushokugocode)
      )
    yield onDone()

  private def updateNameUI(): Unit =
    nameSpan(
      innerText := DiseaseUtil.diseaseNameOf(byoumeiMaster, shuushokugoMasters)
    )

  private def doByoumeiSelected(m: ByoumeiMaster): Unit =
    byoumeiMaster = m
    updateNameUI()

  private def doShuushokugoSelected(m: ShuushokugoMaster): Unit =
    if !shuushokugoMasters.contains(m) then
      shuushokugoMasters = shuushokugoMasters :+ m
      updateNameUI()
