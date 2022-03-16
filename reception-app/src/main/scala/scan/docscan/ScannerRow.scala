package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.ScannerDevice
import dev.fujiwara.domq.InPlaceEdit

class ScannerRow(devices: List[ScannerDevice]):
  import ScannerRow.*
  private var dataOpt: Option[ScannerDevice] = None
  val inPlaceEdit = new InPlaceEdit(new Disp, new Edit(devices), dataOpt)
  val row = new Row
  row.title("スキャナー")
  row.content(inPlaceEdit.ele)

object ScannerRow:
  class Disp:
    val disp = span
    val editLink = a
    val ele = div("DISP")

  object Disp:
    given ElementProvider[Disp] = _.ele
    given DataAcceptor[Disp, Option[ScannerDevice]] =
      DataAcceptor.by(_.disp, _.map(_.description).getOrElse("（選択されていません）"))
    given TriggerProvider[Disp] =
      TriggerProvider.by(_.editLink)

  class Edit(devices: List[ScannerDevice]):
    val select = new Selection[ScannerDevice, ScannerDevice](identity)
    select.formatter = _.description
    select.set(devices)
    val cancelLink = a
    def ele = div(
      select.ele,
      cancelLink("キャンセル")
    )

  object Edit:
    given ElementProvider[Edit] = _.ele
    given DataAcceptor[Edit, Option[ScannerDevice]] =
      DataAcceptor.by(_.select)
    given DataProvider[Edit, Option[ScannerDevice]] =
      DataProvider.by(_.select)
    given TriggerProvider[Edit] =
      TriggerProvider.by(_.select)
    given GeneralTriggerProvider[Edit, "cancel"] =
      GeneralTriggerProvider.by(_.cancelLink)
