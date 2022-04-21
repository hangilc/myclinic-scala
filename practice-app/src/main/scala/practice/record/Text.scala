package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.Text as ModelText
import dev.myclinic.scala.webclient.{Api, global}

class Text(origText: ModelText):
  val ele = div()
  disp(origText)

  def disp(text: ModelText): Unit =
    val d = new TextDisp(text)
    d.ele(onclick := (() => edit(text)))
    ele(clear, d.ele)

  def edit(text: ModelText): Unit =
    val e = new TextEdit(text, disp _, () => disp(text))
    ele(clear, e.ele)

class TextDisp(text: ModelText):
  val ele = div(innerText := text.content)

class TextEdit(text: ModelText, onDone: ModelText => Unit, onCancel: () => Unit):
  val ta = textarea(value := text.content, cls := "practice-text-edit-textarea")
  val ele = div(
    ta,
    div(
      a("入力", onclick := (onEnter _)),
      a("キャンセル", onclick := onCancel),
      a("削除"),
      a("コピー"),
    )
  )

  def onEnter(): Unit =
    val t = new ModelText(text.textId, text.visitId, ta.value.trim)
    for
      _ <- Api.updateText(t)
      up <- Api.getText(t.textId)
    yield onDone(up)
    
