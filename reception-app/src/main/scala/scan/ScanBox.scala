package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.fujiwara.domq.Selection
import dev.myclinic.scala.model.{Patient, Sex, ScannerDevice}
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ScanBox:
  var patientOption: Option[Patient] = None
  val eSearchInput: HTMLInputElement = inputText()
  val eSearchResult: Selection[Patient] =
    Selection[Patient](onSelect = patient => {
      eSearchResult.hide()
      setSelectedPatient(patient)
    })
  val eSelectedPatient: HTMLElement = div()
  val eScannerSelect: HTMLElement = select()
  val eScanTypeSelect: HTMLElement = select()
  val eScanProgress: HTMLElement = span(displayNone)
  val eScanned: HTMLElement = div()
  val ele = div(cls := "scan-box")(
    div(cls := "search-area")(
      h2("患者選択"),
      form(onsubmit := (onSearch _))(
        eSearchInput,
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
      eScannerSelect,
      button("更新", onclick := (refreshScannerSelect _))
    ),
    div(cls := "scan-progress-area")(
      button("スキャン開始", onclick := (onStartScan _)),
      eScanProgress
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

  def init(): Future[Unit] =
    for
      _ <- refreshScannerSelect()
    yield ()

  private def reportProgress(loaded: Double, total: Double): Unit =
    val pct = loaded / total * 100
    eScanProgress.innerText = s"${pct}%"

  private def onStartScan(): Unit = 
    val deviceId: String = eScannerSelect.getSelectValue()
    val resolution = 100
    eScanProgress.innerText = "スキャンの準備中"
    eScanProgress(displayDefault)
    for
      file <- Api.scan(deviceId, (reportProgress _), 100)
    yield 
      eScanProgress.innerText = ""
      eScanProgress(displayNone)

  private def setScannerSelect(devices: List[ScannerDevice]): Unit =
    eScannerSelect.setChildren(
      devices.map(device => {
        option(device.name, value := device.deviceId)
      })
    )

  private def refreshScannerSelect(): Future[Unit] =
    for
      devices <- Api.listScannerDevices()
    yield 
      setScannerSelect(devices)

  private def onSearch(): Unit = 
    val txt = eSearchInput.value.trim
    if !txt.isEmpty then
      for
        patients <- Api.searchPatient(txt)
      yield 
        eSearchResult.clear()
        patients.foreach(addSearchResult(_))
        eSearchResult.show()
        eSearchResult.ele.scrollTop = 0

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
