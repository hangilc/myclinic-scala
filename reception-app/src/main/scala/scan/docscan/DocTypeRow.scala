package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.InPlaceEdit
import scala.language.implicitConversions

class DocTypeRow(using ds: DataSources):
  import DocTypeRow.*
  val disp = new Disp
  val edit = new Edit
  val inPlaceEdit = new InPlaceEdit(
    disp,
    edit,
    None,
    (data: Data) => ds.docType.update(data.map(_._2))
  )
  val row = new Row
  row.title("文書の種類")
  row.content(inPlaceEdit.ele)

  def ele = row.ele
  def selected: Option[String] = inPlaceEdit.getData.map(_._2)

object DocTypeRow:
  type Data = Option[(String, String)]

  class Disp(using ds: DataSources):
    var onEdit: () => Unit = () => ()
    val label = span
    val editLink = a()
    val ele = div(
      label,
      editLink(onclick := (() => {
        ShowMessage.confirmIf(
          !ds.isDocTypeChangeable,
          "文書の種類が変更できない状態ですが、それでも変更を試みますか？"
        )(onEdit)
      }))
    )
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
    given DataAcceptor[Disp, Data] with
      def setData(t: Disp, opt: Data): Unit =
        t.set(opt.map(_._1))
    given TriggerProvider[Disp] with
      def setTriggerHandler(t: Disp, handler: () => Unit): Unit =
        t.onEdit = handler

  class Edit:
    val onSelect = new LocalEventPublisher[Data]
    val cancelLink = a
    val select: Selection[(String, String)] = new Selection[(String, String)]
    select.clear()
    select.addAll(defaultItems, _._1)
    select.addSelectEventHandler(s => onSelect.publish(Some(s)))
    val ele = div(
      select.ele,
      div(cancelLink("キャンセル"))
    )

  object Edit:
    given ElementProvider[Edit] = _.ele
    given DataAcceptor[Edit, Data] =
      (t: Edit, opt: Data) =>
        opt match {
          case Some(d) => t.select.select(d)
          case None    => t.select.unmark()
        }
    given DataProvider[Edit, Data] =
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
