package dev.myclinic.scala.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.Meisai
import dev.myclinic.scala.model.MeisaiSectionData

object RcptUtil:
  def rcptDetail(meisai: Meisai): HTMLElement =
    div(
      meisai.items.map(itemEle _)
    )

  def itemEle(data: MeisaiSectionData): HTMLElement =
    div(
      div(data.section.label),
      data.entries.map(entry => 
        div(
        div(entry.label),
        div(s"${entry.tanka} x ${entry.count}"),
        div(s"${entry.tanka * entry.count}")
        )
      )
    )

