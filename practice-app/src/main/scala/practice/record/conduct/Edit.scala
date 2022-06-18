package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import scala.language.implicitConversions

case class Edit(c: ConductEx):
  val ele = div(cls := "practice-widget",
    div(cls := "practice-widget-title", "処置編集"),
    div(innerText := ConductHelper.rep(c)),
    div(cls := "practice-widget-commands",
      button("削除", onclick := (onDelete _)),
        button ("キャンセル", onclick := (onCancel _))
    )
  )

  def onCancel(): Unit =
    ele.replaceBy(Disp(c).ele)

  def onDelete(): Unit =
    ShowMessage.confirm("この処置を削除してもいいですか？")(() =>
      for
        _ <- Api.deleteConductEx(c.conductId)
      yield 
        PracticeBus.conductDeleted.publish(c.toConduct)
    )


