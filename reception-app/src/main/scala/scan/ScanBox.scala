package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.fujiwara.domq.{Selection, ErrorBox, ShowMessage, CustomEvent}
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
import cats.*
import cats.syntax.all.*
import dev.fujiwara.domq.Icons

class ScanBox():
  var patientOpt: Option[Patient] = None
  var kind: String = "image"
  var scanAllowedGlobally = true
  var isUploading: Boolean = false
  val eSearchInput: HTMLInputElement = inputText()
  val searchResult: Selection[Patient] =
    Selection[Patient](onSelect = patient => {
      searchResult.hide()
      patientOpt = Some(patient)
      updateUI()
    })
  val patientDisp = new SelectedPatientDisp()
  val eScannerSelect: HTMLElement = select()
  val eScanTypeSelect: HTMLElement = select()
  val eScanButton: HTMLElement = button()
  val eScanProgress: HTMLElement = span(displayNone)
  val eScanned: HTMLElement = div()
  val eUploadButton: HTMLElement = button()
  val eCloseButton: HTMLElement = button()
  val scannedItems = new ScannedItems(onUpload _)
  val components: List[ScanBoxUIComponent] = List(
    patientDisp,
    new ScanBoxUIComponent:
      def updateUI(state: ScanBoxState): Unit =
        val enabled: Boolean = scanAllowedGlobally && selectedDevice.isDefined
        eScanButton(disabled := !enabled),
    scannedItems,
    new ScanBoxUIComponent:
      def updateUI(state: ScanBoxState): Unit =
        val enabled = !isUploading && scannedItems.hasUnUploadedImage
        eUploadButton(disabled := !enabled)
  )
  val ele = div(cls := ScanBox.cssClassName)(
    div(cls := "search-area")(
      h2("患者選択"),
      form(onsubmit := (onSearch _))(
        eSearchInput,
        button(attr("type") := "default")("検索")
      )
    ),
    searchResult.ele(cls := "search-result", displayNone),
    patientDisp.ele(cls := "selected-patient"),
    div(cls := "scan-type-area")(
      h2("文書の種類"),
      eScanTypeSelect(onchange := (onScanTypeChange _))
    ),
    div(cls := "scanner-selection-area")(
      h2("スキャナ選択"),
      eScannerSelect,
      button("更新", onclick := (() => {
        (for
          _ <- refreshScannerSelect()
        yield updateUI()).onComplete {
          case Success(_) => ()
          case Failure(ex) => System.err.println(ex.getMessage)
        }
      }))
    ),
    div(cls := "scan-progress-area")(
      eScanButton(
        "スキャン開始",
        onclick := (startScan _),
        cls := ScanBox.cssScanButtonClass
      ),
      eScanProgress
    ),
    eScanned.ele(cls := "scanned")(
      scannedItems.ele,
      eUploadButton("アップロード")(
        cls := "upload-button",
        onclick := (onItemsUpload _),
        disabled := true
      )
    ),
    div(cls := "command-box")(
      eCloseButton("キャンセル", onclick := (onCloseClick _))
    )
  )
  ele.listenToCustomEvent[Boolean]("globally-enable-scan", enable => {
    scanAllowedGlobally = enable
    updateUI()
  })
  addDefaultScanTypes()
  eScanTypeSelect.setSelectValue("image")
  updateUI()

  def init(): Future[Unit] =
    for _ <- refreshScannerSelect()
    yield updateUI()

  private def currentState: ScanBoxState =
    ScanBoxState(patientOpt, kind, selectedDevice)

  private def selectedDevice: Option[String] =
    eScannerSelect.getOptionalSelectValue()

  def updateUI(): Unit =
    val state: ScanBoxState = currentState
    println(("state", state))
    components.foreach(_.updateUI(state))

  private def onItemsUpload(): Unit =
    scannedItems.upload()

  private def onScanTypeChange(): Unit =
    kind = eScanTypeSelect.getSelectValue()
    scannedItems.updateUI()

  private def reportProgress(loaded: Double, total: Double): Unit =
    val pct = loaded / total * 100
    eScanProgress.innerText = s"${pct}%"

  private def startScan(): Unit =
    ele.dispatchEvent(CustomEvent("scan-started", (), true))
    val deviceId: String = selectedDevice.get
    val resolution = 100
    eScanProgress.innerText = "スキャンの準備中"
    eScanProgress(displayDefault)
    (for file <- Api.scan(deviceId, (reportProgress _), 100)
    yield
      eScanProgress.innerText = ""
      eScanProgress(displayNone)
      scannedItems.add(file)
    ).transform(r => {
      ele.dispatchEvent(CustomEvent("scan-ended", (), true))
      r
    }).onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

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
      eSearchInput.value = ""
      for patients <- Api.searchPatient(txt)
      yield
        if patients.size == 1 then
          patientOpt = Some(patients.head)
          searchResult.clear()
          searchResult.hide()
          updateUI()
        else
          searchResult.clear()
          patients.foreach(addSearchResult(_))
          searchResult.show()
          searchResult.ele.scrollTop = 0

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

  private def showSearchResult(): Unit = searchResult.show()
  private def hideSearchResult(): Unit = searchResult.hide()

  def addSearchResult(patient: Patient): Unit =
    searchResult.add(
      formatPatient(patient),
      patient
    )

  private def formatPatient(patient: Patient): String =
    String.format("(%04d) %s", patient.patientId, patient.fullName())

  private def onCloseClick(): Unit =
    def doClose(): Unit =
      val parent = ele.parentNode
      ele.remove()
      parent.dispatchEvent(CustomEvent("scan-box-close", (), true))
    if scannedItems.hasUnUploadedImage then
      ShowMessage.confirm("アップロードされていない画像がありますが、このまま閉じますか？")(doClose _)
    else doClose()

  private def onUpload(done: Boolean): Unit =
    if done then eCloseButton.innerText = "閉じる"
    else eCloseButton.innerText = "キャンセル"

object ScanBox:
  val cssClassName: String = "scan-box"
  val cssScanButtonClass: String = "scan-button"

class ScannedItems(
    onUpload: Boolean => Unit
) extends ScanBoxUIComponent:
  val timestamp: String = makeTimeStamp()
  val eItemsWrapper: HTMLElement = div()
  val errBox = ErrorBox()
  val ele = div(eItemsWrapper, errBox.ele)
  var items: List[ScannedItem] = List.empty

  def updateUI(state: ScanBoxState): Unit =
    ???

  private def makeTimeStamp(): String =
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

  def add(savedFile: String): Unit =
    val index: () => Option[Int] = () =>
      if items.size == 0 then None else Some(items.size + 1)
    val item = new ScannedItem(
      savedFile,
      timestamp,
      items.size + 1,
      () => items.size,
      () => patientRef().map(_.patientId),
      kindRef
    )
    items = items :+ item
    if items.size == 2 then items.foreach(_.updateUI())
    eItemsWrapper(item.ele)

  def updateUI(): Unit = items.foreach(_.updateUI())

  def uploadItems(items: List[ScannedItem]): Future[Unit] =
    items match {
      case Nil => Future.successful(())
      case h :: t =>
        h.ensureUploaded().flatMap(_ => uploadItems(t))
    }

  def upload(): Unit =
    errBox.hide()
    patientRef() match {
      case None => errBox.show("患者が選択されていません。")
      case Some(patient) =>
        uploadItems(items)
          .onComplete {
            case Success(_)  => onUpload(true)
            case Failure(ex) => System.err.println(ex.getMessage)
          }
    }

  def hasUnUploadedImage: Boolean =
    items.find(!_.isUploaded).isDefined
