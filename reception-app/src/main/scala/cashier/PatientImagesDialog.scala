package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.util.NumberUtil.format
import dev.myclinic.scala.model.FileInfo
import dev.fujiwara.domq.SelectionConfig
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.util.FileUtil
import org.scalajs.dom.window

case class PatientImagesDialog(patient: Patient, files: List[FileInfo]):
  import PatientImagesDialog.{mkItem, display}
  val dlog = new ModalDialog3()
  val selectConfig = new SelectionConfig{
    override def enableSingleResultAutoSelect: Boolean = false
  }
  val select = Selection[FileInfo](
    files.map(mkItem),
    (onSelect _)
  )(using selectConfig)
  dlog.title(s"保存画像（${patient.fullName()}）")
  dlog.body(
    div(button("閉じる", onclick := (() => dlog.close()))),
    select.ele
  )

  def open(): Unit =
    dlog.open()

  def onSelect(file: FileInfo): Unit =
    val patientId = patient.patientId
    val fname = file.name
    val url = s"/api/patient-image?patient-id=${patientId}&file-name=${fname}"
    if FileUtil.findFileExtension(fname) == Some("pdf") then
      window.open(url, "_blank")
    else
      display(url, patient, file.name)

object PatientImagesDialog:
  def open(patient: Patient): Unit =
    for
      files <- Api.listPatientImage(patient.patientId)
    yield
      val dlog = new PatientImagesDialog(patient, files)
      dlog.open()

  def mkItem(file: FileInfo): (HTMLElement, FileInfo) =
    (div(file.name), file)

  case class Display(url: String, patient: Patient, filename: String):
    val img = Html.img(cls := "practice-patient-image")
    img.src = url
    val dlog = new ModalDialog3()
    dlog.title("画像閲覧")
    dlog.body(
      div(button("閉じる", onclick := (() => dlog.close()))),
      div(s"(${patient.patientId}) ${patient.fullName()}"),
      div(filename),
      div(
        img
      )
    )

    def open(): Unit = 
      dlog.open()

  def display(url: String,patient: Patient, filename: String): Unit =
    val d = Display(url, patient, filename)
    d.open()


