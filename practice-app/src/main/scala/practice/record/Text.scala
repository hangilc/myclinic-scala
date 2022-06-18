package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.Text as ModelText
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.appbase.PrintDialog
import dev.fujiwara.scala.drawer.PaperSize
import dev.myclinic.scala.formatshohousen.FormatShohousen
import dev.myclinic.scala.formatshohousen.Shohou
import scala.language.implicitConversions

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
  val ele = div(
    innerText := createContent,
    cls := "practice-text-disp"
  )

  def createContent: String = 
    text.content match {
      case "" => "（空白）"
      case c if FormatShohousen.isShohou(c) => FormatShohousen.parse(c).formatForDisp
      case c => c
    }

case class TextEdit(
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
    ) ++ opt(
      Text.isHikitsugi(text.content),
      List(a("引継ぎコピー", onclick := (doCopyHikitsugi _))),
      List.empty
    ) ++ opt(
      FormatShohousen.isShohou(text.content),
      List(shohouLink),
      List.empty
    ) ++ List(
      a("削除", onclick := (doDelete _)),
      a("コピー", onclick := (doCopy _))
    )

  private def opt[T](test: Boolean, yes: T, no: T): T =
    if test then yes else no

  def shohouLink: HTMLElement =
    val pullDown = new PullDownLink("処方箋")
    pullDown.setBuilder(
      List(
        "処方箋発行" -> (doShohousen _),
        "編集中表示" -> (doViewShohousen _)
      )
    )
    pullDown.link

  def onEnter(): Unit =
    import dev.myclinic.scala.util.FunUtil.*
    val content: String = ta.value
      |> FormatShohousen.parse
      |> ((shohou: Shohou) => shohou.formatForSave)
    val t = new ModelText(text.textId, text.visitId, content)
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
        for entered <- Api.enterText(t)
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

  def doShohousen(): Unit =
    for
      ops <- Api.shohousenDrawer(text.textId)
    yield
      val dlog = PrintDialog("処方箋印刷", ops, PaperSize.A5, "shohousen")
      dlog.open()

  def doViewShohousen(): Unit =
    val c = FormatShohousen.parse(ta.value).formatForSave
    val t = text.copy(content = c)
    for
      ops <- Api.shohousenDrawerText(t)
    yield
      val dlog = PrintDialog("処方箋表示", ops, PaperSize.A5, "shohousen")
      dlog.open()

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
