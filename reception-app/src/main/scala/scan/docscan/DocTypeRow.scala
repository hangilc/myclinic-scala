package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.InPlaceEdit

class DocTypeRow:
  import DocTypeRow.*
  val disp = new Disp
  val edit = new Edit
  val inPlaceEdit = new InPlaceEdit(disp, edit, None)
  val row = new Row
  row.title("文書の種類")
  row.content(inPlaceEdit.ele)
  def ele = row.ele
  def selected: Option[String] = inPlaceEdit.getData.map(_._2)

object DocTypeRow:
  class Disp:
    var onEdit: () => Unit = () => ()
    val label = span
    val editLink = a()
    val ele = div(label, editLink(onclick := (() => onEdit())))
    def set(nameOpt: Option[String]): Unit =
      nameOpt match {
        case Some(name) => 
          label(innerText := name)
          editLink(innerText := "[変更]")
        case None => 
          label(innerText := "（選択されていません）")
          editLink(innerText := "[設定]")
      }

  object Disp:
    given ElementProvider[Disp] = _.ele
    given DataAcceptor[Disp, Option[(String, String)]] with
      def setData(t: Disp, opt: Option[(String, String)]): Unit =
        t.set(opt.map(_._1))
    given TriggerProvider[Disp] with
      def setTriggerHandler(t: Disp, handler: () => Unit): Unit =
        t.onEdit = handler

  class Edit:
    var onSelect: Option[(String, String)] => Unit = _ => ()
    var onCancel: () => Unit = () => ()
    val select = new Selection[(String, String), (String, String)](identity)
    select.formatter = _._1
    select.set(defaultItems)
    select.addSelectEventHandler(s => onSelect(Some(s)))
    val ele = div(
      select.ele,
      div(a("キャンセル", onclick := (() => onCancel())))
    )
  
  object Edit:
    given ElementProvider[Edit] = _.ele
    given DataAcceptor[Edit, Option[(String, String)]] with
      def setData(t: Edit, opt: Option[(String, String)]): Unit =
        opt match {
          case Some(data) => t.select.select(data)
          case None => t.select.clearSelected()
        }
    given DataProvider[Edit, Option[(String, String)]] with
      def getData(t: Edit): Option[(String, String)] =
        t.select.selected
    given TriggerProvider[Edit] with
      def setTriggerHandler(t: Edit, handler: () => Unit): Unit =
        t.onSelect = _ => handler()
    given GeneralTriggerProvider[Edit, "cancel"] with
      def setTriggerHandler(t: Edit, handler: () => Unit): Unit =
        t.onCancel = handler

  val defaultItems: List[(String, String)] =
    List(
      "保険証" -> "hokensho",
      "健診結果" -> "health-check",
      "検査結果" -> "exam-report",
      "紹介状" -> "refer",
      "訪問看護指示書など" -> "shijisho",
      "訪問看護などの報告書" -> "zaitaku",
      "その他" -> "image"
    )

