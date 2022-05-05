package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Text as ModelText}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus

class TextEnter(visitId: Int):
  val onDone = new LocalEventPublisher[Unit]
  val ta = textarea
  val ele = div(
    ta(cls := "practice-enter-text-textarea"),
    div(
      button("入力", onclick := (doEnter _)),
      button("キャンセル", onclick := (() => onDone.publish(())))
    )
  )

  def doEnter(): Unit =
    val txt = ta.value
    val m = ModelText(0, visitId, txt)
    for
      entered <- Api.enterText(m)
    yield
      PracticeBus.textEntered.publish(entered)
      onDone.publish(())

object TextEnter:
  def apply(visitId: Int): TextEnter = new TextEnter(visitId)