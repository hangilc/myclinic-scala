package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.ScannerDevice
import dev.fujiwara.domq.InPlaceEdit
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLSpanElement
import cats.*
import cats.syntax.all.*

class ScannerRow(using ds: DataSources):
  import ScannerRow.*
  private var dataOpt: Option[ScannerDevice] = None
  val edit: Edit = new Edit
  val inPlaceEdit = new InPlaceEdit(new Disp, edit, dataOpt, device => 
    ds.scanner.update(device)
  )
  val row = new Row
  row.title("スキャナー")
  row.content(inPlaceEdit.ele)
  def ele = row.ele
  edit.onRefresh.subscribe(_ => {
    listScanner(list => 
      edit.setDevices(list)
      if list.size == 1 then edit.select.mark(list(0))
    )
  })
  listScanner(list => 
    edit.setDevices(list)
    if list.size == 1 then edit.select.select(list(0))
  )

  def listScanner(cb: List[ScannerDevice] => Unit): Unit =
    if ds.mock.data then
      cb(List(
        new ScannerDevice("", "Mock scanner", "Mock scanner"),
        // new ScannerDevice("", "Mock scanner", "Mock scanner 2"),
      ))
    else
      ScannerList.list(cb)

object ScannerRow:
  type Data = Option[ScannerDevice]

  class Disp:
    val disp: HTMLSpanElement = span
    val editLink: HTMLAnchorElement = a
    val ele = div(disp, editLink)

  object Disp:
    given ElementProvider[Disp] = _.ele
    given DataAcceptor[Disp, Data] =
      DataAcceptor[HTMLSpanElement, String]
        .contraInst[Disp](_.disp)
        .contramap[Data](_.map(_.description).getOrElse("（選択されていません）"))
      |+|
      DataAcceptor[HTMLAnchorElement, String]
        .contraInst[Disp](_.editLink)
        .contramap[Data](_.fold("[選択]")(_ => "[変更]"))
    given TriggerProvider[Disp] = TriggerProvider.by(_.editLink)

  class Edit:
    val onRefresh = new LocalEventPublisher[Unit]
    val select: Selection[ScannerDevice] =
      Selection[ScannerDevice]()
    val formatter: ScannerDevice => String = _.description
    val cancelLink = a
    val ele = div(
      select.ele,
      cancelLink("キャンセル"),
      a("更新", onclick := (() => { onRefresh.publish(()); () })),
      a("選択解除", onclick := (() => onUnselect()))
    )

    def setDevices(list: List[ScannerDevice]): Unit =
      select.clear()
      select.addAll(list, formatter, identity)

    private def onUnselect(): Unit =
      select.unselect()

  object Edit:
    given ElementProvider[Edit] = _.ele
    given DataAcceptor[Edit, Data] =
      DataAcceptor.by(_.select)
    given DataProvider[Edit, Data] =
      DataProvider.by(_.select)
    given TriggerProvider[Edit] =
      TriggerProvider.by(_.select)
    given GeneralTriggerProvider[Edit, "cancel"] =
      GeneralTriggerProvider.by(_.cancelLink)
