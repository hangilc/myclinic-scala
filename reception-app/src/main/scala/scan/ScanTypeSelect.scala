package dev.myclinic.scala.web.reception.scan

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Selection}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global

abstract class ScanTypeSelect:
  val eScanTypeSelect = select()
  val ele = div(cls := "scan-type-area")(
    h2("文書の種類"),
    eScanTypeSelect(onchange := (onSelectChange _))
  )
  addDefaultScanTypes()
  eScanTypeSelect.setSelectValue("image")

  def onChange(scanType: String): Unit

  def selected: String =
    eScanTypeSelect.getSelectValue()

  private def onSelectChange(): Unit =
    val scanType = eScanTypeSelect.getSelectValue()
    onChange(scanType)

  private def addDefaultScanTypes(): Unit =
    populateScanTypes(ScanTypeSelect.defaultItems)

  private def populateScanTypes(items: List[(String, String)]): Unit =
    eScanTypeSelect.addChildren(
      items.map({ case (name, optValue) =>
        option(name, value := optValue).ele
      })
    )

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

