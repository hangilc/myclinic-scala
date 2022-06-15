package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.fujiwara.domq.CompAppendList
import dev.myclinic.scala.model.ShuushokugoMaster
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.myclinicutil.DiseaseUtil
import org.scalajs.dom.HTMLLabelElement
import dev.fujiwara.dateinput.EditableDate
import math.Ordering.Implicits.infixOrderingOps

case class Tenki(
    list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])]
):
  import Tenki.Item
  val curWrapper = div
  val currents: CompAppendList[Item] =
    CompAppendList[Item](curWrapper, list.map(Item.apply.tupled(_)))
  private var endDate = LocalDate.now()
  val endDateEle = EditableDate(endDate, "終了日")
  val endReasonGroup = RadioGroup[DiseaseEndReason](
    List(
      "治癒" -> DiseaseEndReason.Cured,
      "中止" -> DiseaseEndReason.Stopped,
      "死亡" -> DiseaseEndReason.Dead
    )
  )
  val ele = div(
    curWrapper,
    div(endDateEle.ele, Icons.calendar(cls := "cursor-pointer")),
    div(
      a("週"),
      a("今日"),
      a("月末"),
      a("先月末")
    ),
    endReasonGroup.ele,
    button("入力")
  )
  currents.list.foreach(
    _.checkLabel.addOnInputListener(_ =>
      endDate = (currents.list
        .foldLeft(None: Option[LocalDate])((acc, item) =>
          (acc, item.checkLabel.selected) match {
            case (None, s)    => s.map(_._2)
            case (Some(d), None) => Some(d)
            case (Some(a), Some(b)) => Some(a.max(b._2))
          }
        ))
        .getOrElse(LocalDate.now())
      updateUI()
    )
  )

  def updateUI(): Unit =
    endDateEle.set(endDate)

object Tenki:
  case class Item(label: String, diseaseId: Int, startDate: LocalDate):
    val checkLabel =
      CheckLabel[(Int, LocalDate)]((diseaseId, startDate), stuffLabel _)
    val ele = div(
      checkLabel.wrap(span)
    )
    def stuffLabel(e: HTMLLabelElement): Unit =
      e(
        label,
        span(
          DateUtil.formatDate(startDate),
          cls := "practice-disease-tenki-start-date"
        )
      )

  object Item:
    def apply(
        d: Disease,
        m: ByoumeiMaster,
        adjList: List[(DiseaseAdj, ShuushokugoMaster)]
    ): Item =
      Item(
        DiseaseUtil.diseaseNameOf(m, adjList.map(_._2)),
        d.diseaseId,
        d.startDate
      )

    given Comp[Item] = _.ele
    given Dispose[Item] = _ => ()
