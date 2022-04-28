package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.Text as ModelText
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import org.scalajs.dom.HTMLElement

class Text(origText: ModelText):
  val ele = div()
  disp(origText)

  def disp(text: ModelText): Unit =
    val d = new TextDisp(text)
    d.ele(onclick := (() => edit(text)))
    ele(clear, d.ele)

  def edit(text: ModelText): Unit =
    val e = new TextEdit(
      text,
      onDone = (disp _),
      onCancel = () => disp(text),
      onDelete = () => ele.remove()
    )
    ele(clear, e.ele)

class TextDisp(text: ModelText):
  val c = if text.content.isEmpty then "（空白）" else text.content
  val ele = div(innerText := c)

class TextEdit(
    text: ModelText,
    onDone: ModelText => Unit,
    onCancel: () => Unit,
    onDelete: () => Unit
):
  val ta = textarea(value := text.content, cls := "practice-text-edit-textarea")
  val ele = div(
    ta,
    div(children := makeLinks)
  )

  def makeLinks: List[HTMLElement] =
    List(
      a("入力", onclick := (onEnter _)),
      a("キャンセル", onclick := onCancel)
    ) ++ (if Text.isHikitsugi(text.content) then
            List(a("引継ぎコピー", onclick := (doCopyHikitsugi _)))
          else List.empty)
      ++ List(
        a("削除", onclick := (doDelete _)),
        a("コピー", onclick := (doCopy _))
      )

  def onEnter(): Unit =
    val t = new ModelText(text.textId, text.visitId, ta.value.trim)
    for
      _ <- Api.updateText(t)
      up <- Api.getText(t.textId)
    yield onDone(up)

  def doCopyHikitsugi(): Unit =
    val target = PracticeBus.copyTarget match {
      case None => 
        ShowMessage.showError("コピー先をみつけられません。")
      case Some(visitId) =>
        val hikitsugi = Text.extractHikitsugi(text.content)
        val t = ModelText(0, visitId, hikitsugi)
        for
          entered <- Api.enterText(t)
        yield 
          PracticeBus.textEntered.publish(entered)
          onCancel()
    }

  def doDelete(): Unit =
    ShowMessage.confirm("この文章を削除していいですか？")(() => {
      for _ <- Api.deleteText(text.textId)
      yield onDelete()
    })

  def doCopy(): Unit =
    PracticeBus.copyTarget match {
      case Some(visitId) =>
        val t = ModelText(0, visitId, text.content)
        for entered <- Api.enterText(t)
        yield
          PracticeBus.textEntered.publish(entered)
          onCancel()
      case None => ShowMessage.showError("コピー先をみつけられません。")
    }

object Text:
  given Comp[Text] = _.ele
  given Dispose[Text] = _ => ()

  def isHikitsugi(s: String): Boolean =
    s.startsWith("●") || s.startsWith("★")

  def extractHikitsugi(s: String): String =
    val pat = "\\n\\s*\\n".r
    pat.findFirstMatchIn(s) match {
      case None    => ""
      case Some(m) => s.substring(0, m.start)
    }
