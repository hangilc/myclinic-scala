package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import org.scalajs.dom.FormData
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.util.FileUtil
import java.time.LocalDateTime
import dev.myclinic.scala.util.PatientImageUtil
import scala.language.implicitConversions

case class PatientImageUploadIdalog(patientId: Int):
  val form = Html.form
  form.target = "/api/upload-patient-image"
  val tagInput = input
  val tagPullDown = PullDown.pullDownLink("例", List(
    "画像" -> (() => tagExample("image")),
    "保険証" -> (() => tagExample("hokensho")),
    "健診結果" -> (() => tagExample("checkup")),
    "在宅報告" -> (() => tagExample("zaitaku")),
    "同意書" -> (() => tagExample("douisho"))
  ))
  val fileInput = input
  val dlog = new ModalDialog3()
  dlog.title("画像保存")
  dlog.body(
    div(
      span("Tag:"),
      tagInput,
      tagPullDown
    ),
    form(
      fileInput(attr("type") := "file", name := "uploadfile", attr("multiple") := "")
    )
  )
  dlog.commands(
    button("アップロード", onclick := (doSubmit _)),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open(): Unit =
    dlog.open()

  def doSubmit(): Unit =
    val at = LocalDateTime.now()
    val formData = new FormData()
    val files = fileInput.files
    files.zipWithIndex.map {
      case (f, i) => 
        val ext = FileUtil.findFileExtension(f.name).get
        val fn = PatientImageUtil.composeFileName(patientId, tag, at, i+1, ext)
        formData.append(s"uploadfile-${i+1}", f, fn)
    }
    Api.uploadPatientImage(patientId, formData).onComplete {
      case Success(_) => dlog.close()
      case Failure(ex) => ShowMessage.showError(ex.toString)
    }

  def tagExample(tag: String): Unit =
    tagInput.value = tag

  def tag: String =
    tagInput.value match {
      case "" => "image"
      case (t) => t
    }