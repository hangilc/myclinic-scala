package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.Selection
import dev.myclinic.scala.model.{Patient, Sex}
import java.time.LocalDate

class ScanBox:
  var patientOption: Option[Patient] = None
  val eSearchResult: Selection[Patient] =
    Selection[Patient](onSelect = patient => {
      eSearchResult.hide()
      setSelectedPatient(patient)
    })
  val eSelectedPatient: HTMLElement = div()
  val eScanTypeSelect: HTMLElement = select()
  val eScanned: HTMLElement = div()
  val ele = div(cls := "scan-box")(
    div(cls := "search-area")(
      h2("患者選択"),
      form(
        inputText(),
        button(attr("type") := "default")("検索")
      )
    ),
    eSearchResult.ele(cls := "search-result", displayNone),
    eSelectedPatient(cls := "selected-patient"),
    div(cls := "scan-type-area")(
      h2("文書の種類"),
      eScanTypeSelect
    ),
    div(cls := "scanner-selection-area")(
      h2("スキャナ選択"),
      select(
        option("Brother DCP-J577N")
      ),
      button("更新")
    ),
    div(cls := "scan-progress-area")(
      button("スキャン開始"),
      span("スキャンの準備中")
    ),
    eScanned(cls := "scanned")(
      div(cls := "scanned-item")(
        "????-image-20211219162626.jpg",
        a("表示"),
        a("再スキャン"),
        a("削除")
      ),
      button(cls := "upload-button")("アップロード")
    ),
    div(cls := "command-box")(
      button("キャンセル")
    )
  )
  addDefaultScanTypes()
  eScanTypeSelect.setSelectValue("image")
  if true then {
    for i <- 1 to 10 do addSearchResult(mockPatient)
    eSearchResult.ele(displayDefault)
  }

  def mockPatient: Patient = Patient(
    1234,
    "田中",
    "孝志",
    "たなか",
    "たかし",
    Sex.Male,
    LocalDate.of(2002, 3, 12),
    "東京",
    "03"
  )

  private def addDefaultScanTypes(): Unit =
    val items = List(
      "保険証" -> "hokensho",
      "健診結果" -> "health-check",
      "検査結果" -> "exam-report",
      "紹介状" -> "refer",
      "訪問看護指示書など" -> "shijisho",
      "訪問看護などの報告書" -> "zaitaku",
      "その他" -> "image"
    )
    populateScanTypes(items)

  def populateScanTypes(items: List[(String, String)]): Unit =
    eScanTypeSelect.addChildren(
      items.map({ case (name, optValue) =>
        option(name, value := optValue).ele
      })
    )

  private def showSearchResult(): Unit = eSearchResult.show()
  private def hideSearchResult(): Unit = eSearchResult.hide()

  def addSearchResult(patient: Patient): Unit =
    eSearchResult.add(
      formatPatient(patient),
      patient
    )

  private def setSelectedPatient(patient: Patient): Unit =
    patientOption = Some(patient)
    eSelectedPatient(innerText := formatPatient(patient))

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())
