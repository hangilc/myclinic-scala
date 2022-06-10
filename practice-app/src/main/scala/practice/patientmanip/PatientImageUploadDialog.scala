package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import org.scalajs.dom.FormData

case class PatientImageUploadIdalog():
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
    val formData = new FormData()
    val files = fileInput.files
    files.zipWithIndex.map {
      case (f, i) => 
        val fn = s"file-${i+1}"
        formData.append(s"uploadfile-${i+1}", f, fn)
    }
    val init = new org.scalajs.dom.RequestInit{}
    init.method = org.scalajs.dom.HttpMethod.POST
    init.body = formData
    org.scalajs.dom.fetch("/api/upload-file?dir=scan-dir", init)

  def tagExample(tag: String): Unit =
    ???