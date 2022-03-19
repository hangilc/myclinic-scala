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
    val onSelect = new LocalEventPublisher[Option[(String, String)]]
    val cancelLink = a
    val select: Selection[(String, String)] = new Selection[(String, String)]
    select.clear()
    select.addAll(defaultItems, _._1, identity)
    select.addSelectEventHandler(s => onSelect.publish(Some(s)))
    val ele = div(
      select.ele,
      div(cancelLink("キャンセル"))
    )
  
  object Edit:
    type Data = Option[(String, String)]
    given ElementProvider[Edit] = _.ele
    given DataAcceptor[Edit, Data] =
      (t: Edit, opt: Data) => 
        opt match {
          case Some(d) => t.select.select(d)
          case None => t.select.unmark()
        }
    given DataProvider[Edit, Option[(String, String)]] =
      DataProvider.by((edit: Edit) => edit.select)
    given TriggerProvider[Edit] =
      TriggerProvider.by((edit: Edit) => edit.select)
    given GeneralTriggerProvider[Edit, "cancel"] =
      GeneralTriggerProvider.by((edit: Edit) => edit.cancelLink)

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

