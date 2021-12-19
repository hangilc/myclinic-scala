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
import java.time.LocalDateTime
import org.scalajs.dom.raw.URL
import org.scalajs.dom.{Blob, BlobPropertyBag}
import scala.scalajs.js
import org.scalajs.dom.ext.Image
import org.scalajs.dom.raw.HTMLImageElement
import org.scalajs.dom.raw.Event
import scala.util.Success
import scala.util.Failure

class ScanBox:
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
  val scannedItems = new ScannedItems()
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
      eScanTypeSelect(onchange := (onScanTypeChange _))
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
    eScanned.ele(cls := "scanned")(
      scannedItems.ele,
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
    for _ <- refreshScannerSelect()
    yield ()

  private def onScanTypeChange(): Unit = 
    val value = eScanTypeSelect.getSelectValue()
    scannedItems.setKind(value)

  private def reportProgress(loaded: Double, total: Double): Unit =
    val pct = loaded / total * 100
    eScanProgress.innerText = s"${pct}%"

  private def onStartScan(): Unit =
    val deviceId: String = eScannerSelect.getSelectValue()
    val resolution = 100
    eScanProgress.innerText = "スキャンの準備中"
    eScanProgress(displayDefault)
    for file <- Api.scan(deviceId, (reportProgress _), 100)
    yield
      eScanProgress.innerText = ""
      eScanProgress(displayNone)
      scannedItems.add(file)

  private def setScannerSelect(devices: List[ScannerDevice]): Unit =
    eScannerSelect.setChildren(
      devices.map(device => {
        option(device.name, value := device.deviceId)
      })
    )

  private def refreshScannerSelect(): Future[Unit] =
    for devices <- Api.listScannerDevices()
    yield setScannerSelect(devices)

  private def onSearch(): Unit =
    val txt = eSearchInput.value.trim
    if !txt.isEmpty then
      for patients <- Api.searchPatient(txt)
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
    eSelectedPatient(innerText := formatPatient(patient))
    scannedItems.setPatient(patient)

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

case class ScannedItem(var savedFile: String, var uploadFile: String):
  val ele = div(cls := "scanned-item")(
      uploadFile,
      a("表示", onclick := (onShow _)),
      a("再スキャン"),
      a("削除")
    )

  private def onShow(): Unit =
    val f = 
      for
        data <- Api.getScannedFile(savedFile)
      yield
        println(("data", data))
        val oURL = URL.createObjectURL(
          new Blob(js.Array(data), BlobPropertyBag("image/jpeg"))
        )
        println(("oURL", oURL))
        val image = org.scalajs.dom.document.createElement("img").asInstanceOf[HTMLImageElement]
        image.onload = (e: Event) => {
          URL.revokeObjectURL(oURL)
        }
        image.src = oURL
        println(("image", image))
        org.scalajs.dom.document.body.appendChild(image)
    f.onComplete {
      case Success(_) => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }


class ScannedItems:
  val ele = div(
  )
  var items: List[ScannedItem] = List.empty
  var patientOpt: Option[Patient] = None
  var kind: String = "image"
  val stamp: String = makeStamp

  def setPatient(patient: Patient): Unit =
    patientOpt = Some(patient)

  def setKind(value: String): Unit =
    kind = value

  private def makeStamp: String = 
      val at = LocalDateTime.now()
      String.format(
      "%d%02d%02d%02d%02d%02d",
      at.getYear,
      at.getMonthValue,
      at.getDayOfMonth,
      at.getHour,
      at.getMinute,
      at.getSecond
    )

  private def uploadFileName(index: Option[Int]): String =
    val pat = patientOpt match {
      case Some(p) => p.patientId.toString
      case None    => "????"
    }
    val ser = index match {
      case Some(i) => s"(${i})"
      case None    => ""
    }
    s"${pat}-${kind}-${stamp}${ser}.jpg"

  def add(savedFile: String): Unit =
    val index = if items.size == 0 then None else Some(items.size + 1)
    val item = new ScannedItem(savedFile, uploadFileName(index))
    items = items :+ item
    ele(item.ele)
