package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.ScannerDevice
import dev.fujiwara.domq.InPlaceEdit
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLSpanElement
import cats.*
import cats.syntax.all.*

class ScannerRow:
  import ScannerRow.*
  private var dataOpt: Option[ScannerDevice] = None
  val inPlaceEdit = new InPlaceEdit(new Disp, (new Edit).init, dataOpt)
  val row = new Row
  row.title("スキャナー")
  row.content(inPlaceEdit.ele)
  def ele = row.ele

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
    private var devices: List[ScannerDevice] = List.empty
    val select: Selection[ScannerDevice, ScannerDevice] = Selection[ScannerDevice]()
    select.formatter = _.description
    val cancelLink = a
    val ele = div(
      select.ele,
      cancelLink("キャンセル")
    )
    def init: Edit =
      ScannerList.get(list => {
        devices = list
        select.set(devices)
      })
      this

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
