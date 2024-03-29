package dev.myclinic.scala.web.reception.scan.patientimages

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.web.reception.scan.{PatientDisp}
import scala.concurrent.Future
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import dev.myclinic.scala.web.reception.scan.ScanBox
import scala.language.implicitConversions

class PatientImages:
  val box = new ScanBox
  box.title("患者画像")
  def ele = box.ele

// class PatientImagesOrig(ui: PatientImagesOrig.UI, patient: Patient):
//   import PatientImagesOrig.ImageList
//   val ele = ui.ele
//   val patientDisp = new PatientDisp
//   val imageList = new ImageList(ui.imageListUI)

//   patientDisp.setPatient(patient)
//   imageList.onSelectCallback = (onSelect _)
//   ui.eCloseButton(onclick := (onClose _))

//   def init(): Future[Unit] =
//     for
//       files <- Api.listPatientImage(patient.patientId)
//     yield 
//       files.sortBy(_.createdAt).reverse.foreach(info => imageList.add(info.name))

//   def onSelect(file: String): Unit =
//     val url = s"/api/get-patient-image?patient-id=${patient.patientId}&file-name=${file}"
//     if file.endsWith(".pdf") then
//       org.scalajs.dom.window.open(url, "_blank")
//       ui.eImageDisp(clear)
//     else
//       val img = dev.fujiwara.domq.all.img(attr("src") := url)
//       ui.eImageDisp(clear, img)

//   def onClose(): Unit =
//     ui.ele.remove()

// object PatientImagesOrig:
//   val itemCssClassName = "scan-patient-images-item"
//   class ImageItemUI:
//     val ele = div(cls := itemCssClassName)

//   class ImageItem(ui: ImageItemUI, file: String):
//     ui.ele(file)

//   class ImageListUI:
//     val ele = div(cls := "scan-patient-images-list")

//   class ImageList(ui: ImageListUI):
//     var onSelectCallback: String => Unit = _ => ()

//     def add(file: String): Unit =
//       val itemUI = new ImageItemUI
//       val item = new ImageItem(itemUI, file)
//       itemUI.ele(onclick := (() => 
//         onSelectCallback(file)
//         removeSelected()
//         itemUI.ele(cls := "selected")
//         ()
//       ))
//       ui.ele(itemUI.ele)

//     def removeSelected(): Unit =
//       ui.ele.qSelectorAll(s".${itemCssClassName}").foreach(_(cls :- "selected"))

  // class UI:
  //   val patientDisp = new PatientDisp
  //   val imageListUI = new ImageListUI
  //   val eImageDisp = div
  //   val eCloseButton = button

  //   val ele = div(cls := "scan-patient-images")(
  //     div("保存画像", fontWeight := "bold"),
  //     patientDisp.ele,
  //     imageListUI.ele,
  //     eImageDisp,
  //     div(textAlign := "end")(
  //       eCloseButton("閉じる")
  //     )
  //   )

  // def apply(patient: Patient): PatientImagesOrig =
  //   val ui = new UI
  //   new PatientImagesOrig(ui, patient)


