package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import dev.fujiwara.kanjidate.KanjiDate
import scala.language.implicitConversions

case class Edit(
    list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])],
    onDone: (Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)]) => Unit
):
  import Edit.{Item, Disp}
  val disp = Disp()
  val selection = Selection.make[Item](list.map(Item.apply.tupled(_)), _.ele)
  selection.addSelectEventHandler(item =>
    disp.disease = Some(item.disease)
    disp.byoumeiMaster = Some(item.byoumeiMaster)
    disp.adjList = item.adjList
    disp.updateUI()  
  )
  val ele = div(
    disp.ele,
    div(button("編集", onclick := (doEdit _))),
    selection.ele
  )

  def doEdit(): Unit =
    for
      d <- disp.disease
      m <- disp.byoumeiMaster
    yield onDone(d, m, disp.adjList)

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
      var disease: Option[Disease] = None,
      var byoumeiMaster: Option[ByoumeiMaster] = None,
      var adjList: List[(DiseaseAdj, ShuushokugoMaster)] = List.empty
  ):
    val nameEle = span
    val startDateEle = span
    val endReasonEle = span
    val endDateEle = span

    val ele = div(
      cls := "grid-disp",
      div("名称："),
      nameEle,
      div("開始日："),
      startDateEle,
      div("転機："),
      endReasonEle,
      div("終了日："),
      endDateEle
    )

    def updateUI(): Unit =
      updateNameUI()
      updateStartDateUI()
      updateEndReasonUI()
      updateEndDateUI()

    def updateNameUI(): Unit =
      val name =
        (for m <- byoumeiMaster
        yield DiseaseUtil.diseaseNameOf(m, adjList.map(_._2))).getOrElse("")
      nameEle(innerText := name)

    def updateStartDateUI(): Unit =
      val startDate = 
        (for
          d <- disease
        yield KanjiDate.dateToKanji(d.startDate)).getOrElse("")
      startDateEle(innerText := startDate)

    def updateEndReasonUI(): Unit =
      val endReason = 
        (for
          d <- disease
        yield d.endReason.label).getOrElse("")
      endReasonEle(innerText := endReason)

    def updateEndDateUI(): Unit =
      val endDate =
        (for
          d <- disease
          e <- d.endDate.value
        yield KanjiDate.dateToKanji(e)).getOrElse("")
      endDateEle(innerText := endDate)
