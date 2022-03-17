package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.ScannerDevice
import dev.fujiwara.domq.InPlaceEdit
import org.scalajs.dom.HTMLAnchorElement
import org.scalajs.dom.HTMLSpanElement

class ScannerRow:
  import ScannerRow.*
  private var dataOpt: Option[ScannerDevice] = None
  val inPlaceEdit = new InPlaceEdit(new Disp, new Edit, dataOpt)
  val row = new Row
  row.title("スキャナー")
  row.content(inPlaceEdit.ele)
  def ele = row.ele

object ScannerRow:
  type Data = Option[ScannerDevice]

  class Disp:
    val disp = span
    val editLink = a
    val ele = div(disp, editLink)

  object Disp:
    import cats.*
    import cats.implicits.*
    given ElementProvider[Disp] = _.ele
    given DataAcceptor[Disp, Data] =
      DataAcceptor[Disp, Data]() |+|
        DataAcceptor.by(
          t => t.disp,
          d => d.map(_.description).getOrElse("（選択されていません）")
        ) |+|
        DataAcceptor.by(
          t => t.editLink,
          _ match {
            case Some(_) => "変更"
            case None    => "選択"
          }
        )
    given TriggerProvider[Disp] =
      TriggerProvider.by(_.editLink)

  class Edit:
    var devices: List[ScannerDevice] = List.empty
    val select = new Selection[ScannerDevice, ScannerDevice](identity)
    select.formatter = _.description
    select.set(devices)
    val cancelLink = a
    val ele = div(
      select.ele,
      cancelLink("キャンセル")
    )

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
