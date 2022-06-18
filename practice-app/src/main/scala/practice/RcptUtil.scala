package dev.myclinic.scala.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.Meisai
import dev.myclinic.scala.model.MeisaiSectionData
import scala.language.implicitConversions

object RcptUtil:
  def rcptDetail(meisai: Meisai): HTMLElement =
    div(
      meisai.items.map(detailItemEle _),
      hr(cls := "practice-rcpt-detail-hr"),
      detailSummary(meisai)
    )

  private def detailItemEle(data: MeisaiSectionData): HTMLElement =
    div(
      div(data.section.label),
      data.entries.map(entry =>
        div(
          cls := "practice-rcpt-detail-item",
          div(entry.label, cls := "practice-rcpt-detail-item-label"),
          div(
            s"${entry.tanka}x${entry.count}",
            cls := "practice-rcpt-detail-item-tanka-count"
          ),
          div(
            s"${entry.tanka * entry.count}",
            cls := "practice-rcpt-detail-item-total"
          )
        )
      )
    )

  private def detailSummary(meisai: Meisai): HTMLElement =
    val t =
      s"総点：${meisai.totalTen}点、負担割：${meisai.futanWari}割、自己負担：${meisai.charge}円"
    div(t, cls := "practice-rcpt-detail-summary")
