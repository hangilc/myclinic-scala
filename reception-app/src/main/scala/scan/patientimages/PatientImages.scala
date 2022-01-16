package dev.myclinic.scala.web.reception.scan.patientimages

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.web.reception.scan.{PatientDisp}
import scala.concurrent.Future
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class PatientImages(ui: PatientImages.UI, patient: Patient):
  import PatientImages.ImageList
  val ele = ui.ele
  val patientDisp = new PatientDisp(ui.patientDispUI)
  val imageList = new ImageList(ui.imageListUI)

  patientDisp.setPatient(patient)
  imageList.onSelectCallback = (onSelect _)

  def init(): Future[Unit] =
    for
      files <- Api.listPatientImage(patient.patientId)
    yield 
      files.foreach(imageList.add(_))

  def onSelect(file: String): Unit =
    val url = s"/api/get-patient-image?patient-id=${patient.patientId}&file-name=${file}"
    if file.endsWith(".pdf") then
      org.scalajs.dom.window.open(url, "_blank")
    else
      val img = dev.fujiwara.domq.all.img(attr("src") := url)
      ui.eImageDisp.setChildren(img)

object PatientImages:
  class ImageItemUI:
    val ele = div

  class ImageItem(ui: ImageItemUI, file: String):
    ui.ele(file)

  class ImageListUI:
    val ele = div

  class ImageList(ui: ImageListUI):
    var onSelectCallback: String => Unit = _ => ()

    def add(file: String): Unit =
      val itemUI = new ImageItemUI
      val item = new ImageItem(itemUI, file)
      itemUI.ele(onclick := (() => onSelectCallback(file)))
      ui.ele(itemUI.ele)

  class UI:
    val patientDispUI = new PatientDisp.UI
    val imageListUI = new ImageListUI
    val eImageDisp = div

    val ele = div(cls := "patient-images")(
      div("保存画像", fontWeight := "bold"),
      patientDispUI.ele,
      imageListUI.ele,
      eImageDisp
    )

  def apply(patient: Patient): PatientImages =
    val ui = new UI
    new PatientImages(ui, patient)


