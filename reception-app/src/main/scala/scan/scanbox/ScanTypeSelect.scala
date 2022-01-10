package dev.myclinic.scala.web.reception.scan.scanbox

import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Html
import dev.fujiwara.domq.Html.*
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection}
import scala.language.implicitConversions

class ScanTypeSelect(ui: ScanTypeSelect.UI):
  var onChangeCallback: String => Unit = _ => ()
  ui.eScanTypeSelect(onchange := (() => 
    val t = ui.eScanTypeSelect.getValue
    onChangeCallback(t)
  ))
  def getValue: String = ui.eScanTypeSelect.getValue
  def setValue(value: String): Boolean = ui.eScanTypeSelect.setValue(value)

object ScanTypeSelect:
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

  class UI:
    val eScanTypeSelect = select
    val ele = div(cls := "scan-type-area")(
      h2("文書の種類"),
      eScanTypeSelect
    )
    addDefaultScanTypes()
    
    private def addDefaultScanTypes(): Unit =
      populateScanTypes(defaultItems)

    private def populateScanTypes(items: List[(String, String)]): Unit =
      eScanTypeSelect.addChildren(
        items.map({ case (name, optValue) =>
          option(name, value := optValue)
        })
      )


