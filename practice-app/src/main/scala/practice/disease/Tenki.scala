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
import dev.myclinic.scala.webclient.{Api, global}
import cats.syntax.all.*
import org.scalajs.dom.MouseEvent

case class Tenki(
    list: List[(Disease, ByoumeiMaster, List[(DiseaseAdj, ShuushokugoMaster)])],
    onDone: Tenki => Unit
):
  import Tenki.Item
  val curWrapper = div
  val currents: CompAppendList[Item] =
    CompAppendList[Item](curWrapper, list.map(Item.apply.tupled(_)))
  val endDateEle = EditableDate(LocalDate.now(), "終了日")
  def endDate: LocalDate = endDateEle.date
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
      a("週", onclick := (doWeek _)),
      a("今日", onclick := (doToday _)),
      a("月末", onclick := (doEndOfMonth _)),
      a("先月末", onclick := (doEndOfLastMonth _))
    ),
    endReasonGroup.ele,
    button("入力", onclick := (doEnter _))
  )
  currents.list.foreach(
    _.checkLabel.addOnInputListener(_ =>
      val endDate: LocalDate = (currents.list
        .foldLeft(None: Option[LocalDate])((acc, item) =>
          (acc, item.checkLabel.selected) match {
            case (None, s)    => s.map(_._2)
            case (Some(d), None) => Some(d)
            case (Some(a), Some(b)) => Some(a.max(b._2))
          }
        ))
        .getOrElse(LocalDate.now())
      endDateEle.set(endDate)
    )
  )

  def doWeek(event: MouseEvent): Unit =
    if event.shiftKey then
      endDateEle.incDays(-7)
    else
      endDateEle.incDays(7)

  def doToday(): Unit =
    endDateEle.set(LocalDate.now())

  def doEndOfMonth(): Unit =
    endDateEle.set(endDate.withDayOfMonth(1).plusMonths(1).plusDays(-1))

  def doEndOfLastMonth(): Unit =
    endDateEle.set(LocalDate.now().withDayOfMonth(1).plusDays(-1))

  def endReason: DiseaseEndReason =
    endReasonGroup.selected

  def doEnter(): Unit =
    val diseaseIds = currents.list.map(_.checkLabel.selected).collect {
      case Some((diseaseId, _)) => diseaseId
    }
    val reason: DiseaseEndReason = endReason
    for
      _ <- diseaseIds.map(diseaseId => Api.endDisease(diseaseId, endDate, reason)).sequence_
    yield onDone(this)

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
