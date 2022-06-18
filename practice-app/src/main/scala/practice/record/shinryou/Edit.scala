package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import scala.language.implicitConversions

case class Edit(shinryou: ShinryouEx):
  val ele = div(cls := "practice-widget",
    div("診療行為編集", cls := "practice-widget-title"),
    div(innerText := shinryou.master.name),
    div(cls := "practice-widget-commands",
      button("削除", onclick := (onDelete _)),
      button("キャンセル", onclick := (onCancel _))
    )
  )

  def onCancel(): Unit =
    ele.replaceBy(Disp(shinryou).ele)

  def onDelete(): Unit =
    for
      _ <- Api.deleteShinryou(shinryou.shinryouId)
    yield 
      PracticeBus.shinryouDeleted.publish(shinryou.toShinryou)